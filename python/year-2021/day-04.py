#!/usr/bin/env python3

def check_bingo(grid, called):
    for r in range(0, len(grid), 5):
        if all(x in called for x in grid[r:r + 5]):
            return True
    for c in range(5):
        if all(x in called for x in grid[c::5]):
            return True
    return False


def score(grid, called):
    return sum([x for x in grid if x not in called])


def part1():
    for called in (calls[:i] for i in range(1, len(calls))):
        for grid in grids:
            if check_bingo(grid, called):
                return called[-1] * score(grid, called)


def part2():
    playing = grids[:]
    for called in (calls[:i] for i in range(1, len(calls))):
        results = [(grid, check_bingo(grid, called)) for grid in playing]
        remaining = [grid for (grid, bingo) in results if not bingo]
        if not remaining:
            wins = [grid for (grid, bingo) in results if bingo]
            return called[-1] * score(wins[-1], called)
        playing = remaining


with open('../../../aoc-21-inputs/year-2021/day-04/input.txt') as f:
    lines = f.readlines()

calls = [int(n) for n in lines[0].split(',')]
grids = [
    [int(n) for n in chunk if len(n)]
    for chunk in (
        (x.strip() for x in ' '.join(lines[ofs:ofs + 6]).split(' '))
        for ofs in range(1, len(lines), 6)
    )
]

print(part1())
print(part2())
