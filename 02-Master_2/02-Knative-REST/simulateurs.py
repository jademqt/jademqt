import requests
import json

# Function to get data
def get_data():
    url = 'http://localhost:37639'
    response = requests.get(url)
    if response.status_code == 200:
        data = response.json()
        print("Received data:", data)
    else:
        print("Failed to get data. Status code:", response.status_code)

# Function to post data
def post_data(table, releve):
    url = 'http://localhost:37639'
    data = {table: releve}  
    headers = {'Content-Type': 'application/json'}
    response = requests.post(url, data=json.dumps(data), headers=headers)
    if response.status_code == 200:
        print("Data posted successfully")
    else:
        print("Failed to post data. Status code:", response.status_code)

# Call the functions
if __name__ == "__main__":
    get_data()
    #post_data("humidity", "70.5")
