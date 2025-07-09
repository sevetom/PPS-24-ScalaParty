move((X,Y), (X1,Y)) :- X1 is X+1, walkable(X1,Y).
move((X,Y), (X1,Y)) :- X1 is X-1, walkable(X1,Y).
move((X,Y), (X,Y1)) :- Y1 is Y+1, walkable(X,Y1).
move((X,Y), (X,Y1)) :- Y1 is Y-1, walkable(X,Y1).

bfs([[Goal|Path]|_], Goal, FullPath) :- reverse([Goal|Path], FullPath).

bfs([CurrentPath|OtherPaths], Goal, Path) :-
    CurrentPath = [Current|_],
    findall(
        [Next|CurrentPath],
        (move(Current, Next), 
        \+ member(Next, CurrentPath)),
        NewPaths
    ),
    append(OtherPaths, NewPaths, Queue),
    bfs(Queue, Goal, Path).

solve_maze(Path) :- entry(SX,SY), exit(GX,GY), bfs([[(SX,SY)]], (GX,GY), Path).
