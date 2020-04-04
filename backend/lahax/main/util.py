from itertools import chain, combinations


def powerset(iterable):
    "powerset([1,2,3]) --> () (1,) (2,) (3,) (1,2) (1,3) (2,3) (1,2,3)"
    s = list(iterable)
    return chain.from_iterable(combinations(s, r) for r in range(len(s)+1))


def powerset_length_split(iterable):
    s = list(powerset(iterable))
    
    max_length = 0
    for __set in s:
        if len(__set) > max_length:
            max_length = len(__set)

    out = [0] * (max_length + 1)
    
    for __set in s:
        if out[len(__set)] == 0:
            out[len(__set)] = []

        out[len(__set)].append(__set)
    
    return out
