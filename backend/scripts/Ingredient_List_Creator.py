import csv


s = set()

with open('RAW_recipes.csv', encoding ='utf-8') as csv_file:
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

i = open('ingredients.csv', 'w+')
for x in s:
    i.write(x + ',')

i.close()