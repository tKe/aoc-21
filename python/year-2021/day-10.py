#!/usr/bin/env python3
from functools import reduce
from statistics import median

pairs = {'(': ')', '{': '}', '[': ']', '<': '>'}
with open('../../../aoc-21-inputs/year-2021/day-10/example.txt') as f:
    inputs = [x.strip() for x in f.readlines()]


def part1():
    def score_input(line):
        scores = {')': 3, ']': 57, '}': 1197, '>': 25137}
        stack = []
        for c in line:
            if c in pairs:
                stack.append(pairs[c])
            elif c != stack.pop():
                return scores[c]

    return sum(score for line in inputs if (score := score_input(line)))


def part2():
    def score_input(line):
        scores = {')': 1, ']': 2, '}': 3, '>': 4}
        stack = []
        for c in line:
            if c in pairs:
                stack.append(pairs[c])
            elif c != stack.pop():
                break
        else:
            return reduce(lambda acc, s: acc * 5 + scores[s], stack, 0)

    return median(score for line in inputs if (score := score_input(line)))


print(part1())
print(part2())
