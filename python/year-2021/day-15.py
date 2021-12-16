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
    cells = rows * cols

    def neighbours(idx):
        x = idx % cols
        return tuple(i for i in (
            idx - 1 if x > 0 else -1,
            idx + 1 if x < last_col else -1,
            idx - cols,
            idx + cols
        ) if 0 <= i < cells)

    start_idx = start[1] * cols + start[0] if start else 0
    end_idx = end[1] * cols + end[0] if end else cells - 1

    costs = [v for r in grid for v in r]
    dists = [math.inf for r in grid for _ in r]
    dists[start_idx] = 0
    queue = [(0, start_idx)]

    while len(queue):
        (cur_dist, cur_idx) = heappop(queue)
        if cur_idx == end_idx:
            return cur_dist
        for n_idx in neighbours(cur_idx):
            n_dist = cur_dist + costs[n_idx]
            if n_dist < dists[n_idx]:
                dists[n_idx] = n_dist
                heappush(queue, (n_dist, n_idx))


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
