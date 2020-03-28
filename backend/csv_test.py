import csv

s = set()

with open('RAW_recipes.csv') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    line_count = 0
    for row in csv_reader:
        if line_count == 0:
            print(f'Column names are {", ".join(row)}')
            line_count += 1
        else:
            for c in eval(row[10]):
                s.add(c)
            line_count += 1
            print(f'\rProcessed {line_count} lines.', end='')

print(len(s))
