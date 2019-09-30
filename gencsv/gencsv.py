#!/usr/local/bin/python3
import csv
import random

with open("us_presidents.csv") as f:
    reader = csv.reader(f)
    next(reader)
    with open("salary.csv", "w") as g:
        writer = csv.writer(g)
        for row in reader:
            v = round(random.uniform(-100, 5000.0), 2)
            writer.writerow([row[1], v])
