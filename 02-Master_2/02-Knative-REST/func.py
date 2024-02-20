from parliament import Context
from flask import Request
import json
import mysql.connector
import datetime

# Connect to MySQL
mysql_connection = mysql.connector.connect(
    host="rds-knative.ckrcehmpvt3h.eu-west-3.rds.amazonaws.com",
    user="admin",
    password="testtest",
    database="weather_data"
)
mysql_cursor = mysql_connection.cursor()

def parse_and_store_data(request, data_table_name):
    if request.method == "POST" and request.is_json:
        query_response = "Data sent"
        data = request.json
    elif request.method == "POST":
        query_response = "Data sent"
        data = request.form
    elif request.method == "GET":
        return retrieve_data()
    else:
        return "ERROR"
    
    for key, value in data.items():
        date_of_the_day = datetime.date.today()
        formated_date = date_of_the_day.strftime("%Y-%m-%d")
        sql = f"INSERT INTO weather_data.{data_table_name} (data, releve) VALUES (%s, %s)"
        val = (formated_date, value)
        mysql_cursor.execute(sql, val)
        mysql_connection.commit()

    return query_response

def retrieve_data():
    data_store = {}

    #get temperature
    data_table_name="temperature"
    mysql_cursor.execute("SELECT releve FROM weather_data.temperature;")
    rows = mysql_cursor.fetchall()
    for row in rows:
        releve = row[0]
        if data_table_name not in data_store:
            data_store[data_table_name] = []
        data_store[data_table_name].append(releve)
    
    #get luminosity
    data_table_name="luminosity"
    mysql_cursor.execute("SELECT releve FROM weather_data.luminosity;")
    rows = mysql_cursor.fetchall()
    for row in rows:
        releve = row[0]
        if data_table_name not in data_store:
            data_store[data_table_name] = []
        data_store[data_table_name].append(releve)

    #get humidity
    data_table_name="humidity"
    mysql_cursor.execute("SELECT releve FROM weather_data.humidity;")
    rows = mysql_cursor.fetchall()
    for row in rows:
        releve = row[0]
        if data_table_name not in data_store:
            data_store[data_table_name] = []
        data_store[data_table_name].append(releve)

    #return all the data
    return data_store


# parse request body, json data or URL query parameters
def payload_print(req: Request) -> str:
    if req.method == "POST":
        if req.is_json:
            return json.dumps(req.json) + "\n"
        else:
            # MultiDict needs some iteration
            ret = "{"

            for key in req.form.keys():
                ret += '"' + key + '": "'+ req.form[key] + '", '

            return ret[:-2] + "}\n" if len(ret) > 2 else "{}"

    elif req.method == "GET":
        # MultiDict needs some iteration
        ret = "{"

        for key in req.args.keys():
            ret += '"' + key + '": "' + req.args[key] + '", '

        return ret[:-2] + "}\n" if len(ret) > 2 else "{}"


# pretty print the request to stdout instantaneously
def pretty_print(req: Request) -> str:
    ret = str(req.method) + ' ' + str(req.url) + ' ' + str(req.host) + '\n'
    for (header, values) in req.headers:
        ret += "  " + str(header) + ": " + values + '\n'

    if req.method == "POST":
        ret += "Request body:\n"
        ret += "  " + payload_print(req) + '\n'

    elif req.method == "GET":
        ret += "URL Query String:\n"
        ret += "  " + payload_print(req) + '\n'

    return ret

 
def main(context: Context):
    """ 
    Function template
    The context parameter contains the Flask request object and any
    CloudEvent received with the request.
    """

    # Add your business logic here
    print("Received request")
    data_table_name = "temperature"
    if 'request' in context.keys():
        ret = pretty_print(context.request)
        print(ret, flush=True)
        query_response = parse_and_store_data(context.request, data_table_name)
        return query_response, 200
    else:
        print("Empty request", flush=True)
        return json.dumps(data_store), 200
