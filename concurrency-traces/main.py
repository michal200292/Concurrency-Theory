from parse_input import parse
from itertools import product, chain
from collections import deque
import graphviz


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


def topo_sort(graph):
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
        classes[class_ind[i]].append(i)

    return classes


def graph_image(graph, save_dot="", save_png=""):
    dot = graphviz.Digraph(format='png')
    for i in graph.vertices:
        for j in graph.neighbors[i]:
            dot.edge(str(i), str(j))

    for i in graph.vertices:
        dot.node(str(i), graph.trace[i])

    print(dot)
    if save_dot:
        dot.save(f"graph_plots/{save_dot}")
    if save_png:
        dot.render(f"graph_plots/{save_png}", view=True)


def reduce_graph(graph, classes=None):
    if classes is None:
        classes = topo_sort(graph)
    topo_order = list(chain.from_iterable(classes))
    inv_permutation = [0 for _ in range(len(topo_order))]
    for i, v in enumerate(topo_order):
        inv_permutation[v] = i

    for v in graph.vertices:
        graph.neighbors[v].sort(key=lambda x: inv_permutation[x])
    reachable = [{v} for v in graph.vertices]
    for v in topo_order[::-1]:
        filtered = []
        for s in graph.neighbors[v]:
            if s not in reachable[v]:
                filtered.append(s)
                reachable[v] = reachable[v].union(reachable[s])
        graph.neighbors[v] = filtered


def foata_normal_form(graph, classes=None):
    if classes is None:
        classes = topo_sort(graph)
    classes = map(sorted, map(lambda cls: map(lambda x: graph.trace[x], cls), classes))
    fnf = "".join(["(" + "".join([x for x in cls]) + ")" for cls in classes])
    return fnf


def find_dependencies(transact, alphabet):
    dependency = set()
    for a, b in product(alphabet, alphabet):
        if transact[a].left_v == transact[b].left_v or transact[b].left_v in transact[a].right_v:
            dependency.add((a, b))
            dependency.add((b, a))

    independency = set(product(alphabet, alphabet)) - dependency
    return dependency, independency


def solve(filename):
    t, trace, alphabet = parse(f"inputs/{filename}")
    dependency, independency = find_dependencies(t, alphabet)
    print(f"D = {dependency}")
    print(f"I = {independency}")

    graph = Graph(trace, dependency)
    abstract_classes = topo_sort(graph)
    graph_image(graph, "topo_sort_1", "topo_sort_1")

    print(f"FNF([w]) = {foata_normal_form(graph, abstract_classes)}")
    reduce_graph(graph, abstract_classes)
    graph_image(graph, "closure_1", "closure_1")


if __name__ == '__main__':
    solve("example_input")

