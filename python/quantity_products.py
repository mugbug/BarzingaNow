import csv, json
import requests

X = []
Y = []
with open('products.csv', 'rb') as data:
    reader = csv.reader(data, delimiter=',')
    headers = {'content-type' : 'application/x-www-form-urlencoded', 'Authorization' : 'Bearer: Update'}
    params = {'sessionKey': '9ebbd0b25760557393a43064a92bae539d962103', 'format': 'xml', 'platformId': 1}
    for line in reader:
        url = "https://cron-dot-barzinganow.appspot.com/api/product/"
        # url = "http://localhost:8080/api/product/"
        url += line[0]
        url += "/quantity"
        data = {"quantity": int(line[4])}
        print data
        print requests.post(url, data=data, headers=headers, params=params)