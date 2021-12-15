#!/usr/bin/env python3
from heapq import heappop, heappush
import math


def main():
    with open('../../../aoc-21-inputs/year-2021/day-15/input.txt') as f:
        grid = tuple(tuple(int(c) for c in x.strip()) for x in f.readlines())
    print(dijkstra(grid))
    print(dijkstra(scale_grid(grid, 5)))


def dijkstra(grid, start=None, end=None):
    rows, cols = (len(grid), len(grid[0]))
    last_row, last_col = (rows - 1, cols - 1)

    def neighbours(pt):
        x, y = pt
        return (n for n in (
            (x - 1, y) if x > 0 else None,
            (x + 1, y) if x < last_col else None,
            (x, y - 1) if y > 0 else None,
            (x, y + 1) if y < last_row else None
        ) if n)

    start = start or (0, 0)
    end = end or (last_col, last_row)

    dists = [[math.inf for _ in r] for r in grid]
    dists[start[1]][start[1]] = 0
    queue = [(0, start)]

    while len(queue):
        (cur_dist, cur_pt) = heappop(queue)
        if cur_pt == end:
            return cur_dist
        for n_pt in neighbours(cur_pt):
            n_dist = cur_dist + grid[n_pt[1]][n_pt[0]]
            if n_dist < dists[n_pt[1]][n_pt[0]]:
                dists[n_pt[1]][n_pt[0]] = n_dist
                heappush(queue, (n_dist, n_pt))


def scale_grid(grid, scale):
    rows = len(grid)
    cols = len(grid[0])
    return [
        [(grid[y % rows][x % rows] + x // cols + y // rows - 1) % 9 + 1
         for x in range(cols * scale)]
        for y in range(rows * scale)
    ]


if __name__ == '__main__':
    main()
