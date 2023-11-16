import re

p_transaction = re.compile(r"\([a-zA-Z]+\)")
p_left_variable = re.compile(r"[_a-zA-Z]+[_a-zA-Z1-9]*:=")
p_right_variable = re.compile(r"[_a-zA-Z]+[_a-zA-Z1-9]*")
p_alphabet = re.compile(r"\{[,a-zA-Z]+}")


class Transaction:
    def __init__(self, name: str, left_v: str, right_v: set[str]):
        self.name = name
        self.left_v = left_v
        self.right_v = right_v


def parse_transaction(l, transactions):
    t = p_transaction.search(l)
    t = t.group(0)[1:-1]
    l = l.replace(t, "")
    left_v = p_left_variable.search(l).group(0)[:-2]
    l = l.replace(left_v, "")
    right_v = p_right_variable.findall(l)
    transactions[t] = Transaction(t, left_v, set(right_v))


def parse(filename):
    with open(filename, "r", encoding="utf-8") as f:
        file = f.readlines()

    transactions = {}
    trace = ""
    alphabet = set()
    for line in file:
        l = "".join(line.strip().split())
        if not l:
            continue
        if p_transaction.search(l):
            parse_transaction(l, transactions)
        else:
            if t := p_alphabet.search(l):
                t = t.group(0)[1:-1].split(",")
                alphabet = set(t)
                if alphabet < transactions.keys():
                    raise ValueError("Input alphabet differs from defined transactions")
            else:
                trace = l.split("=")[1].strip()

    trace_tokenized = re.findall("|".join(list(transactions.keys())), trace)
    if "".join(trace_tokenized) != trace:
        raise ValueError("Trace contains characters that are not in alphabet")

    return transactions, trace_tokenized, alphabet


if __name__ == "__main__":
    parse("inputs/example_input")
