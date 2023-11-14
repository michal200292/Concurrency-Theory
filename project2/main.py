from parse_input import parse
from itertools import product
from collections import deque


class Graph:
    def __init__(self, trace, depend):
        self.trace = trace.copy()
        self.vertices = list(range(len(trace)))
        self.depend = depend
        self.neighbors = [[] for _ in range(len(trace))]
        self.create_graph()

    def create_graph(self):
        for i in self.vertices:
            for j in self.vertices[i+1:]:
                if (self.trace[i], self.trace[j]) in self.depend:
                    self.neighbors[i].append(j)


def foata_normal_form(graph):
    count_in_edges = [0 for _ in graph.vertices]
    for i in graph.vertices:
        for j in graph.neighbors[i]:
            count_in_edges[j] += 1

    d = deque((i, 0) for i in graph.vertices if count_in_edges[i] == 0)
    class_ind = [0 for _ in graph.vertices]

    while d:
        v, c = d.popleft()
        class_ind[v] = c
        for s in graph.neighbors[v]:
            count_in_edges[s] -= 1
            if count_in_edges[s] == 0:
                d.append((s, c + 1))

    classes = [[] for _ in range(max(class_ind) + 1)]
    for i in graph.vertices:
        classes[class_ind[i]].append(graph.trace[i])

    return "".join(map(lambda l: "({})".format("".join(l)), map(sorted, classes))), classes


if __name__ == '__main__':
    t, trace, alphabet = parse("example_input")
    dependency = set()

    for a, b in product(alphabet, alphabet):
        if t[a].left_v == t[b].left_v or t[b].left_v in t[a].right_v:
            dependency.add((a, b))
            dependency.add((b, a))

    independency = set(product(alphabet, alphabet)) - dependency
    # print(f"D = {dependency}")
    # print(f"I = {independency}")

    graph = Graph(trace, dependency)
    print(foata_normal_form(graph)[0])
