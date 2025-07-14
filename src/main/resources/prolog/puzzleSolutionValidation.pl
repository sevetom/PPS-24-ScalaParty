is_valid_solution(S, W, H) :- 
	retractall(are_adiacent(_, _)),
	retractall(size(_)),
	retractall(are_inside_grid(_, _)),
	SIZE is W * H,
	assert(size(SIZE)),
	assert((are_inside_grid(N1, N2) :- N1 >= 0, N1 < SIZE, N2 >= 0, N2 < SIZE)),
	assert((are_adiacent(N1, N2) :- are_inside_grid(N1, N2), N2 is N1 + 1,  (N2 mod W) =\= 0)),
	assert((are_adiacent(N1, N2) :- are_inside_grid(N1, N2), N2 is N1 - 1,  (N1 mod W) =\= 0)),
	assert((are_adiacent(N1, N2) :- are_inside_grid(N1, N2), N2 is N1 + W, N2 < SIZE)),
	assert((are_adiacent(N1, N2) :- are_inside_grid(N1, N2), N2 is N1 - W, N2 >= 0)),
	contains_valid_pieces(S),
	pieces_are_disjoint_and_complete(S).

pieces_are_disjoint_and_complete(S) :-
	flat(S, F),
	size(SIZE),
	\+ list_has_duplicates(F),
	length(F, SIZE).

contains_valid_pieces([]).
contains_valid_pieces([H|T]) :-
	piece_is_valid(H),
	contains_valid_pieces(T).

piece_is_valid(P) :- length(P, 1), !.
piece_is_valid(P) :-
	findall(X, (member(X, P), is_adiacent(X, P)), L),
	length(L, LEN),
	length(P, N),
	LEN =:= N.

is_adiacent(X, P) :-
	member(Y, P),
	are_adiacent(X, Y), !.

flat([], []).
flat([[]|T], S) :-
	flat(T, S).
flat([[H|T]|T1], [H|S]) :-
	flat([T|T1], S).

list_contains(E, [E|T]) :- !.
list_contains(E, [H|T]) :- list_contains(E, T). 
	
list_has_duplicates([H|T]) :- list_contains(H, T).
list_has_duplicates([H|T]) :- 
	\+ list_contains(H, T),
	list_has_duplicates(T).

