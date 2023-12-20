from enum import Enum
from dataclasses import dataclass


class Op(Enum):
    FIND = 1
    MUL = 2
    SUB = 3


@dataclass(eq=True, frozen=True)
class Node:
    op: Op
    i: int
    j: int
    k: int

    def related(self, other: 'Node') -> bool:
        if self == other:
            return False
        match self.op, other.op:
            case Op.FIND, Op.MUL:
                return (self.i, self.k) == (other.i, other.k)
            case Op.FIND, Op.SUB:
                return (other.k, other.j) in [(self.i, self.i), (self.k, self.i)]
            case Op.SUB, Op.MUL:
                return self.write() == other.read() or (other.i, other.k, other.j) == (self.i, self.k, self.j)
            case Op.SUB, Op.SUB:
                return self.write() == other.write()
        return False

    def read(self) -> tuple[int, int]:
        return self.i, self.j

    def write(self) -> tuple[int, int]:
        return self.k, self.j

    def __repr__(self) -> str:
        match self.op:
            case Op.FIND:
                return f"A{self.i}{self.k}"
            case Op.MUL:
                return f"B{self.i}{self.j}{self.k}"
            case Op.SUB:
                return f"C{self.i}{self.j}{self.k}"

