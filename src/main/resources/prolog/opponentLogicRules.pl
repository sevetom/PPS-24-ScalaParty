valid_position(Pos) :- cell(Pos, _).

adjacent((X,Y), (X2,Y)) :-
    X2 is X + 1,
    valid_position((X2,Y)).
adjacent((X,Y), (X2,Y)) :-
    X2 is X - 1,
    valid_position((X2,Y)).
adjacent((X,Y), (X,Y2)) :-
    Y2 is Y + 1,
    valid_position((X,Y2)).
adjacent((X,Y), (X,Y2)) :-
    Y2 is Y - 1,
    valid_position((X,Y2)).

manhattan((X1,Y1), (X2,Y2), D) :-
    DX is abs(X1 - X2),
    DY is abs(Y1 - Y2),
    D is DX + DY.

%SORT UTILITY
insert_into_sorted((D, X, Y), [], [(D, X, Y)]).
insert_into_sorted((D_new, X_new, Y_new), [(D_head, X_head, Y_head)|T], [(D_new, X_new, Y_new), (D_head, X_head, Y_head)|T]) :-
    D_new =< D_head.
insert_into_sorted((D_new, X_new, Y_new), [(D_head, X_head, Y_head)|T], [(D_head, X_head, Y_head)|SortedT]) :-
    D_new > D_head,
    insert_into_sorted((D_new, X_new, Y_new), T, SortedT).

sort([], []).
sort([H|T], SortedList) :-
    sort(T, SortedTail),
    insert_into_sorted(H, SortedTail, SortedList).

closest_monad(Start, Target) :-
	findall((D, Pos), (
		cell(Pos, monad),
		manhattan(Start, Pos, D)
	), Distances),
	Distances \= [],
	sort(Distances, [(_, Target)|_]).

closest_rung(Start, Target, Monads) :-
	Monads >= 5,
	findall((D, Pos), (
		cell(Pos, rung),
		manhattan(Start, Pos, D)
	), Distances),
	Distances \= [],
	sort(Distances, [(_, Target)|_]).

closest_collectable(Start, Target) :-
    monads_owned(N),
    ( N >= 5 ->
        closest_rung(Start, Target, N)
    ; closest_monad(Start, Target)
    ).

path_bfs(Start, Goal, Path) :-
    bfs_queue([[Start]], Goal, FinalPath),
    reverse(FinalPath, Path).

bfs_queue([[Goal|PathTail] | _], Goal, [Goal|PathTail]).
bfs_queue([[Current|PathTail] | QueueTail], Goal, Path) :-
    findall(
        [Next, Current | PathTail],
        (adjacent(Current, Next), \+ member(Next, [Current|PathTail])),
        NewPaths
    ),
    append(QueueTail, NewPaths, UpdatedQueue),
    bfs_queue(UpdatedQueue, Goal, Path).

best_path(Path) :-
    opponent_pos(Start),
    monads_owned(N),
    closest_collectable(Start, Target),
    path_bfs(Start, Target, Path).