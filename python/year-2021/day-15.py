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
    last_row_idx = last_row * cols
    last_idx = last_row * cols + last_col

    start_idx = start[1] * cols + start[0] if start else 0
    end_idx = end[1] * cols + end[0] if end else last_idx

    costs = tuple(cost for row in grid for cost in row)
    dists = [math.inf] * cols * rows
    dists[start_idx] = 0
    queue = [(0, start_idx)]

    def assess(idx):
        dist = cur_dist + costs[idx]
        if dist < dists[idx]:
            dists[idx] = dist
            heappush(queue, (dist, idx))

    while queue:
        (cur_dist, cur_idx) = heappop(queue)
        if cur_idx == end_idx:
            return cur_dist
        cur_x = cur_idx % cols
        if cur_x > 0: assess(cur_idx - 1)
        if cur_x < last_col: assess(cur_idx + 1)
        if cur_idx >= cols: assess(cur_idx - cols)
        if cur_idx < last_row_idx: assess(cur_idx + cols)


def scale_grid(grid, scale):
    rows = len(grid)
    cols = len(grid[0])
    return tuple(
        tuple((grid[y % rows][x % cols] + x // cols + y // rows - 1) % 9 + 1
              for x in range(cols * scale))
        for y in range(rows * scale)
    )


if __name__ == '__main__':
    main()
