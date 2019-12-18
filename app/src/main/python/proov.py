import pandas as pd
import urllib.request, json

import numpy as np


def getAndmed(path_str):
    with open(path_str) as json_file:
        data = json.load(json_file)
    andmed = data.get("data")
    return andmed

def tabel_andmetest(path):
    rawAndmed = getAndmed(path)
    
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
    
    for taotlus in rawAndmed:
        if taotlus.get("status") == "decision_ok":
            taotlus["status"] = 1
        elif taotlus.get("status") == "decision_not_ok":
            taotlus["status"] = 0
        else:
            taotlus["status"] = None
        
    
    for taotlus in rawAndmed:
        id.append(taotlus.get("id"))
        application_code.append(taotlus.get("application_code"))
        applicationround_title.append(taotlus.get("applicationround_title"))
        approved_amount.append(taotlus.get("approved_amount"))
        cost_statement_submission_date.append(taotlus.get("cost_statement_submission_date"))
        cost_statement_submission_deadline.append(taotlus.get("cost_statement_submission_deadline"))
        managing_organization_name.append(taotlus.get("managing_organization_name"))
        name.append(taotlus.get("name"))
        project_name.append(taotlus.get("project_name"))
        registration_date.append(taotlus.get("registration_date"))
        registry_code.append(taotlus.get("registry_code"))
        requested_amount.append(taotlus.get("requested_amount"))
        status.append(taotlus.get("status"))
        status_txt.append(taotlus.get("status_txt"))
        submission_date.append(taotlus.get("submission_date"))
        domain_code.append(taotlus.get("domain_code"))
        domain_name.append(taotlus.get("domain_name"))
    
    andmed = pd.DataFrame(
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
    return andmed

def biggestFunding(path):
    data = tabel_andmetest(path);
    most_expensive = data.loc[data['approved_amount'].idxmax()]
    applicationround_title = most_expensive["applicationround_title"]
    approved_amount = most_expensive["approved_amount"]
    managing_organization_name = most_expensive["managing_organization_name"]
    name = most_expensive["name"]
    project_name = most_expensive["project_name"]
    return applicationround_title, approved_amount, managing_organization_name, name, project_name

def test(num):
    return num