from itertools import chain
from collections import deque
from copy import deepcopy
from typing import Optional
from Node import Node


class Graph:
    def __init__(self, trace: list['Node']):
        self.trace: list['Node'] = deepcopy(trace)
        self.vertices: list[int] = list(range(len(trace)))
        self.neighbors: list[list[int]] = [[] for _ in range(len(trace))]
        self.reduced: bool = False
        self.classes: Optional[list[list[int]]] = None
        self.create_edges()

    def create_edges(self) -> None:
        for i in self.vertices:
            for j in self.vertices[i + 1:]:
                if self.trace[i].related(self.trace[j]) or self.trace[j].related(self.trace[i]):
                    self.neighbors[i].append(j)

    def topo_sort(self) -> None:
        count_in_edges = [0 for _ in self.vertices]
        for i in self.vertices:
            for j in self.neighbors[i]:
                count_in_edges[j] += 1
        d = deque((i, 0) for i in self.vertices if count_in_edges[i] == 0)
        class_ind = [0 for _ in self.vertices]
        while d:
            v, c = d.popleft()
            class_ind[v] = c
            for s in self.neighbors[v]:
                count_in_edges[s] -= 1
                if count_in_edges[s] == 0:
                    d.append((s, c + 1))
        classes = [[] for _ in range(max(class_ind) + 1)]
        for i in self.vertices:
            classes[class_ind[i]].append(i)
        self.classes = classes

    def reduce_graph(self) -> None:
        if self.classes is None:
            self.topo_sort()
        topo_order = list(chain.from_iterable(self.classes))
        inv_permutation = [0 for _ in range(len(topo_order))]
        for i, v in enumerate(topo_order):
            inv_permutation[v] = i
        for v in self.vertices:
            self.neighbors[v].sort(key=lambda x: inv_permutation[x])
        reachable = [{v} for v in self.vertices]
        for v in topo_order[::-1]:
            filtered = []
            for s in self.neighbors[v]:
                if s not in reachable[v]:
                    filtered.append(s)
                    reachable[v] = reachable[v].union(reachable[s])
            self.neighbors[v] = filtered
        self.reduced = True

    def foata_normal_form(self) -> str:
        if self.classes is None:
            self.topo_sort()
        classes = map(lambda cls: map(lambda x: self.trace[x], cls), self.classes)
        fnf = "".join(["(" + "".join([f"{str(x)} " for x in cls]) + ")" for cls in classes])
        return fnf


