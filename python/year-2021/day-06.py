#!/usr/bin/env python3
from functools import reduce
with open('../../../aoc-21-inputs/year-2021/day-06/input.txt') as f:
    def simulate(state, days): return reduce(lambda s, _: (*s[1:7], s[7] + s[0], s[8], s[0]), range(days), state)
    initial = reduce(lambda acc, age: (*acc[:age], acc[age] + 1, *acc[age + 1:]),
                     (int(x) for x in f.readline().split(',')), (0,) * 9)
    print(sum(simulate(initial, 80)))
    print(sum(simulate(initial, 256)))
