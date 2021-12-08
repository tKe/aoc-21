#!/usr/bin/env python3
from statistics import median, mean
def fuel_usage(crabs: list[int], pos: int, rate=lambda n: n): return int(sum(rate(abs(pos - p)) for p in crabs))
with open('../../../aoc-21-inputs/year-2021/day-07/input.txt') as f:
    crabs = [int(x) for x in f.readline().split(',')]
    print(fuel_usage(crabs, median(crabs)))
    print(min((fuel_usage(crabs, int(mean(crabs)) + x, lambda n: int(n * (n + 1) / 2)) for x in [-1, 0, 1])))
