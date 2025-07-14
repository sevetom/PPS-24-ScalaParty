% Run out of Randoms: recycle
binary_tree_cells(X, Y, Width, Height, [], OriginalRandoms, RemovedWalls) :-
		X < Width,
		Y > 0,
    binary_tree_cells(X, Y, Width, Height, OriginalRandoms, OriginalRandoms, RemovedWalls).

% End
binary_tree_cells(Width, 0, Width, Height, Randoms, OriginalRandoms, [(Width,0,none)]).

% East Most: Remove North and go up a row
binary_tree_cells(Width, Y, Width, Height, Randoms, OriginalRandoms, [(Width,Y,north)|RestOfRemovedWalls]) :-
		Y > 0,
    YNext is Y - 1,
    binary_tree_cells(0, YNext, Width, Height, Randoms, OriginalRandoms, RestOfRemovedWalls).

% North Most: Remove east and continue
binary_tree_cells(X, 0, Width, Height, Randoms, OriginalRandoms, [(X,0,east)|RestOfRemovedWalls]) :-
		X < Width,
    NextX is X + 1,
    binary_tree_cells(NextX, 0, Width, Height, Randoms, OriginalRandoms, RestOfRemovedWalls).     

% Cell inside the grid
% North (0)
binary_tree_cells(X, Y, Width, Height, [0|RestOfRandoms], OriginalRandoms, [(X,Y,north)|RestOfRemovedWalls]) :-
		X < Width,
		Y > 0,
    NextX is X + 1,
    binary_tree_cells(NextX, Y, Width, Height, RestOfRandoms, OriginalRandoms, RestOfRemovedWalls).
%East (1)
binary_tree_cells(X, Y, Width, Height, [1|RestOfRandoms], OriginalRandoms, [(X,Y,east)|RestOfRemovedWalls]) :-
		X < Width,
		Y > 0,
    NextX is X + 1,
    binary_tree_cells(NextX, Y, Width, Height, RestOfRandoms, OriginalRandoms, RestOfRemovedWalls).  

% Generate a binary tree maze north-east oriented
generate_ne_maze(Width, Height, Randoms, RemovedWalls) :-
    Right is Width-1, Bottom is Height-1,
    binary_tree_cells(0, Bottom, Right, Bottom, Randoms, Randoms, RemovedWalls).    
