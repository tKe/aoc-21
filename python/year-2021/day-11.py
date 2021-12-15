#!/usr/bin/env python3
def dump(grid):
    print("---")
    print('\n'.join(''.join(str(x) if x <= 9 else 'F' for x in row) for row in grid))
    print("---")


def update_cells(f, grid):
    for y in range(len(grid)):
        for x in range(len(grid[y])):
            grid[y][x] = f(grid[y][x])


def find_indices(predicate, grid):
    return {
        (x, y)
        for y in range(len(grid))
        for x in range(len(grid[y]))
        if predicate(grid[y][x])
    }


def count_flashed(grid):
    return sum(1 for y in range(len(grid)) for x in range(len(grid[y])) if grid[y][x] == 0)


def neighbours(x, y):
    return (
        (x - 1, y - 1), (x, y - 1), (x + 1, y - 1),
        (x - 1, y), (x + 1, y),
        (x - 1, y + 1), (x, y + 1), (x + 1, y + 1),
    )


def step(grid):
    update_cells(lambda v: v + 1, grid)
    flashed = find_indices(lambda v: v > 9, grid)
    not_flashed = find_indices(lambda v: v <= 9, grid)
    while len(flashed) > 0:
        flashing = find_indices(lambda v: v > 9, grid)
        for (x, y) in (pt for fx, fy in flashed
                       for x, y in neighbours(fx, fy)
                       if (pt := (x, y)) in not_flashed):
            grid[y][x] += 1
        flashed = find_indices(lambda v: v > 9, grid) - flashing
    update_cells(lambda v: 0 if v > 9 else v, grid)


def main():
    with open('../../../aoc-21-inputs/year-2021/day-11/example.txt') as f:
        grid = [[int(v) for v in x.strip()] for x in f.readlines()]

    s = 0
    for i in range(100):
        step(grid)
        s += count_flashed(grid)
    print(s)

    count = 100  # already done 100 step for part1
    while True:
        count += 1
        step(grid)
        if count_flashed(grid) == len(grid) * len(grid[0]):
            break
    print(count)


if __name__ == '__main__':
    main()
