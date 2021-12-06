#!/usr/bin/env python3
import collections, math, itertools, typing
def range_to(a, b):
    s = int(math.copysign(1, b - a))
    return range(a, b + s, s)
def iter_coords(a, b):
    return zip(*(itertools.cycle((a[i],)) if a[i] == b[i] else range_to(a[i], b[i]) for i in range(2)))
def count_dangers(coords, diagonals):
    return sum(1 for _, v in collections.Counter(
        coord for (a, b, c, d) in coords if diagonals or a == c or b == d
        for coord in iter_coords((a, b), (c, d))
    ).items() if v > 1)
with open('../../../aoc-21-inputs/year-2021/day-05/input.txt') as f:
    lines = [tuple(int(x) for x in line.strip().replace(' -> ', ',').split(','))
             for line in f.readlines()]
    print(count_dangers(lines, False))
    print(count_dangers(lines, True))
