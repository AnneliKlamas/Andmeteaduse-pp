import pandas as pd
import urllib.request, json
import numpy as np
from sklearn import preprocessing, metrics
from sklearn.ensemble import RandomForestClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler



def getData(path_str):
    with open(path_str) as json_file:
        data = json.load(json_file)
    andmed = data.get("data")
    return andmed

def data_table(path):
    rawData = getData(path)

    id = []
    application_code = []
    applicationround_title = []
    approved_amount =[]
    cost_statement_submission_date = []
    cost_statement_submission_deadline = []
    managing_organization_name = []
    name = []
    project_name = []
    registration_date = []
    registry_code = []
    requested_amount = []
    status = []
    status_txt = []
    submission_date = []
    domain_code = []
    domain_name =[]

    for request in rawData:
        if request.get("status") == "decision_ok":
            request["status"] = 1
        elif request.get("status") == "decision_not_ok":
            request["status"] = 0
        else:
            request["status"] = None


    for request in rawData:
        id.append(request.get("id"))
        application_code.append(request.get("application_code"))
        applicationround_title.append(request.get("applicationround_title"))
        approved_amount.append(request.get("approved_amount"))
        cost_statement_submission_date.append(request.get("cost_statement_submission_date"))
        cost_statement_submission_deadline.append(request.get("cost_statement_submission_deadline"))
        managing_organization_name.append(request.get("managing_organization_name"))
        name.append(request.get("name"))
        project_name.append(request.get("project_name"))
        registration_date.append(request.get("registration_date"))
        registry_code.append(request.get("registry_code"))
        requested_amount.append(request.get("requested_amount"))
        status.append(request.get("status"))
        status_txt.append(request.get("status_txt"))
        submission_date.append(request.get("submission_date"))
        domain_code.append(request.get("domain_code"))
        domain_name.append(request.get("domain_name"))

    data = pd.DataFrame(
        {#"ID" : id,
        "application_code" : application_code,
        "applicationround_title" : applicationround_title,
        "approved_amount" : approved_amount,
        "cost_statement_submission_date" : cost_statement_submission_date,
        "cost_statement_submission_deadline" : cost_statement_submission_deadline,
        "managing_organization_name" : managing_organization_name,
        "name" : name,
        "project_name" : project_name,
        "registration_date" : registration_date,
        "registry_code" : registry_code,
        "requested_amount" : requested_amount,
        "status" : status,
        "status_txt" : status_txt,
        "submission_date" : submission_date,
        "domain_code" : domain_code,
        "domain_name" : domain_name
        }
    )
    return data

def decisionTree(path):
    table = data_table(path)

    feature_cols = ["requested_amount", "domain_name"]
    for_training = table[(table.status.notnull())]

    X = for_training[feature_cols]
    y = for_training["status"]

    enc1 = preprocessing.LabelEncoder()
    domain_name_encoder = enc1.fit(table.domain_name.unique())
    domain_int = domain_name_encoder.transform(X.domain_name)
    X.domain_name = domain_int

    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size=0.8, random_state=219)
    dtc = DecisionTreeClassifier(class_weight=None, criterion='gini', max_depth=6,
            max_features=None, max_leaf_nodes=None,
            min_impurity_decrease=0.0, min_impurity_split=None,
            min_samples_leaf=1, min_samples_split=6,
            min_weight_fraction_leaf=0, presort=False, random_state=None,
            splitter='best')

    dtc = dtc.fit(X_train, y_train)

    y_pred = dtc.predict(X_test)
    return dtc

def randomforest(path):
    table = data_table(path)

    feature_cols = ["domain_name", "requested_amount"]
    for_training = table[(table.status.notnull())]

    X = for_training[feature_cols]
    y = for_training["status"]

    enc1 = preprocessing.LabelEncoder()
    domain_name_encoder = enc1.fit(table.domain_name.unique())
    domain_int = domain_name_encoder.transform(X.domain_name)
    X.domain_name = domain_int

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.4)
    #X, y = make_classification(n_samples=2000, n_features=4,n_informative=2, n_redundant=0,random_state=0, shuffle=False)
    dlf = RandomForestClassifier(n_estimators=1000, criterion='gini', max_depth=None,
                                 min_samples_split=2, min_samples_leaf=1, min_weight_fraction_leaf=0.0,
                                 max_features='auto', max_leaf_nodes=None, min_impurity_decrease=0.0,
                                 min_impurity_split=None, bootstrap=True, oob_score=False, n_jobs=None,
                                 random_state=318, verbose=0, warm_start=False, class_weight=None)
    dlf = dlf.fit(X_train, y_train)

    y_pred = dlf.predict(X_test)
    return dlf

def knn(path):
    table = data_table(path)
    feature_cols = ["domain_name", "requested_amount"]
    for_training = table[(table.status.notnull())]

    X = for_training[feature_cols]
    y = for_training["status"]

    enc1 = preprocessing.LabelEncoder()
    enc2 = preprocessing.LabelEncoder()
    domain_name_encoder = enc1.fit(table.domain_name.unique())
    managing_org_name_encoder = enc2.fit(table.managing_organization_name.unique())
    domain_int = domain_name_encoder.transform(X.domain_name)
    #managing_organization_name_int = managing_org_name_encoder.transform(X.managing_organization_name)\n",
    X.domain_name = domain_int


    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3)

    scaler = StandardScaler()
    scaler.fit(X_train)
    X_train = scaler.transform(X_train)
    X_test = scaler.transform(X_test)
    classifier = KNeighborsClassifier(n_neighbors=31)
    classifier.fit(X_train, y_train)
    y_pred = classifier.predict(X_test)
    return classifier

def regressionLog(path):

    table = data_table(path)

    feature_cols = ["domain_name", "requested_amount"]
    for_training = table[(table.status.notnull())]

    X = for_training[feature_cols]
    y = for_training["status"]

    enc1 = preprocessing.LabelEncoder()
    domain_name_encoder = enc1.fit(table.domain_name.unique())
    domain_int = domain_name_encoder.transform(X.domain_name)
    X.domain_name = domain_int

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.25)
    logRegr = LogisticRegression()
    logRegr = logRegr.fit(X_train, y_train)
    predictions = logRegr.predict(X_test)
    score = logRegr.score(X_test, y_test)
    print(score)


def guess(for_guessing, path):
    X = for_guessing
    table = data_table(path)
    print(X)

    enc1 = preprocessing.LabelEncoder()
    domain_name_encoder = enc1.fit(table.domain_name.unique())
    domain_int = domain_name_encoder.transform(X.domain_name)
    X.domain_name = domain_int
    print(X)


    teacher = decisionTree(path)
    result = teacher.predict(pd.DataFrame({"domain_name": X["domain_name"], "requested_amount": X["requested_amount"]}))
    return result

def guessData(category, sum, path):

    data = pd.DataFrame({"domain_name" : [category], "requested_amount" : [sum]})
    return guess(data, path)


def biggestFunding(path):
    data = data_table(path)
    most_expensive = data.loc[data['approved_amount'].idxmax()]
    applicationround_title = most_expensive["applicationround_title"]
    approved_amount = most_expensive["approved_amount"]
    managing_organization_name = most_expensive["managing_organization_name"]
    name = most_expensive["name"]
    project_name = most_expensive["project_name"]
    return applicationround_title, approved_amount, managing_organization_name, name, project_name


def smallestFunding(path):
    data = data_table(path)
    data = data.dropna(how = 'all')
    data.drop(data[data['approved_amount'] <= 0].index, inplace = True)
    cheapest = data.loc[data['approved_amount'].idxmin()]
    applicationround_title = cheapest["applicationround_title"]
    approved_amount = cheapest["approved_amount"]
    managing_organization_name = cheapest["managing_organization_name"]
    name = cheapest["name"]
    project_name = cheapest["project_name"]
    return [applicationround_title, approved_amount, managing_organization_name, name, project_name]

def uniqDoms(path):
    data = data_table(path)
    domain_names = data["domain_name"].unique()
    return domain_names
