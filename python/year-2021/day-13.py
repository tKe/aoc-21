#!/usr/bin/env python3
from functools import reduce

def parse_dot(line):
    return tuple(int(x) for x in line.split(','))

def parse_fold(line):
    (axis, n) = line.split(' ')[-1].split('=')
    return axis, int(n)

def fold(dots, axis, n):
    if axis == 'x':
        return {(n - (x - n) if x > n else x, y) for (x, y) in dots}
    if axis == 'y':
        return {(x, n - (y - n) if y > n else y) for (x, y) in dots}

def origami(dots, folds):
    return reduce(lambda acc, f: fold(acc, *f), folds, dots)

def dump(dots):
    print('\n'.join(''.join(u'\u2593' if (x, y) in dots else u'\u2591'
                            for x in range(max(x for (_, x) in dots) + 1))
                    for y in range(max(y for (_, y) in dots) + 1)))

def main():
    with open('../../../aoc-21-inputs/year-2021/day-13/example.txt') as f:
        inputs = [x.strip() for x in f.readlines()]
    split = inputs.index("")
    dots = {parse_dot(line) for line in inputs[:split]}
    folds = [parse_fold(line) for line in inputs[split + 1:]]

    print(sum(1 for _ in fold(dots, *folds[0])))
    dump(origami(dots, folds))

if __name__ == '__main__':
    main()
