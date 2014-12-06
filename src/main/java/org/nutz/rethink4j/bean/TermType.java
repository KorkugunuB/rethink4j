package org.nutz.rethink4j.bean;

public enum TermType {
	DATUM               (  1),
	MAKE_ARRAY          (  2),// DATUM... -> ARRAY
	MAKE_OBJ            (  3),// {...} -> OBJECT
	VAR                 ( 10),// !NUMBER -> DATUM
	JAVASCRIPT          ( 11),// STRING {timeout: !NUMBER} -> DATUM | STRING {timeout: !NUMBER} -> Function(*)
	UUID                (169),// () -> DATUM
	HTTP                (153),// STRING {data: OBJECT | STRING,
	ERROR               ( 12),// STRING -> Error | -> Error
	IMPLICIT_VAR        ( 13),// -> DATUM
	DB                  ( 14),// STRING -> Database
	TABLE               ( 15),// Database, STRING, {use_outdated:BOOL} -> Table | STRING, {use_outdated:BOOL} -> Table
	GET                 ( 16),// Table, STRING -> SingleSelection | Table, NUMBER -> SingleSelection | Table, STRING -> NULL            | Table, NUMBER -> NULL
	GET_ALL             ( 78),// Table, DATUM..., {index:!STRING} -> ARRAY
	EQ                  ( 17),// DATUM... -> BOOL
	NE                  ( 18),// DATUM... -> BOOL
	LT                  ( 19),// DATUM... -> BOOL
	LE                  ( 20),// DATUM... -> BOOL
	GT                  ( 21),// DATUM... -> BOOL
	GE                  ( 22),// DATUM... -> BOOL
	NOT                 ( 23),// BOOL -> BOOL
	ADD                 ( 24),// NUMBER... -> NUMBER | STRING... -> STRING
	SUB                 ( 25),// NUMBER... -> NUMBER
	MUL                 ( 26),// NUMBER... -> NUMBER
	DIV                 ( 27),// NUMBER... -> NUMBER
	MOD                 ( 28),// NUMBER, NUMBER -> NUMBER
	APPEND              ( 29),// ARRAY, DATUM -> ARRAY
	PREPEND             ( 80),// ARRAY, DATUM -> ARRAY
	DIFFERENCE          ( 95),// ARRAY, ARRAY -> ARRAY
	SET_INSERT          ( 88),// ARRAY, DATUM -> ARRAY
	SET_INTERSECTION    ( 89),// ARRAY, ARRAY -> ARRAY
	SET_UNION           ( 90),// ARRAY, ARRAY -> ARRAY
	SET_DIFFERENCE      ( 91),// ARRAY, ARRAY -> ARRAY
	SLICE               ( 30),// Sequence, NUMBER, NUMBER -> Sequence
	SKIP                ( 70),// Sequence, NUMBER -> Sequence
	LIMIT               ( 71),// Sequence, NUMBER -> Sequence
	INDEXES_OF          ( 87),// Sequence, DATUM -> Sequence | Sequence, Function(1) -> Sequence
	CONTAINS            ( 93),// Sequence, DATUM -> BOOL | Sequence, Function(1) -> BOOL
	GET_FIELD           ( 31),// OBJECT, STRING -> DATUM
	KEYS                ( 94),// OBJECT -> ARRAY
	OBJECT              (143),// STRING, DATUM, ... -> OBJECT
	HAS_FIELDS          ( 32),// OBJECT, Pathspec... -> BOOL
	WITH_FIELDS         ( 96),// Sequence, Pathspec... -> Sequence
	PLUCK               ( 33),// Sequence, Pathspec... -> Sequence | OBJECT, Pathspec... -> OBJECT
	WITHOUT             ( 34),// Sequence, Pathspec... -> Sequence | OBJECT, Pathspec... -> OBJECT
	MERGE               ( 35),// OBJECT... -> OBJECT | Sequence -> Sequence
	BETWEEN             ( 36),// StreamSelection, DATUM, DATUM, {index:!STRING, right_bound:STRING, left_bound:STRING} -> StreamSelection
	REDUCE              ( 37),// Sequence, Function(2) -> DATUM
	MAP                 ( 38),// Sequence, Function(1) -> Sequence
	FILTER              ( 39),// Sequence, Function(1), {default:DATUM} -> Sequence | Sequence, OBJECT, {default:DATUM} -> Sequence
	CONCATMAP           ( 40),// Sequence, Function(1) -> Sequence
	ORDERBY             ( 41),// Sequence, (!STRING | Ordering)... -> Sequence
	DISTINCT            ( 42),// Sequence -> Sequence
	COUNT               ( 43),// Sequence -> NUMBER | Sequence, DATUM -> NUMBER | Sequence, Function(1) -> NUMBER
	IS_EMPTY            ( 86),// Sequence -> BOOL
	UNION               ( 44),// Sequence... -> Sequence
	NTH                 ( 45),// Sequence, NUMBER -> DATUM
	BRACKET             (170),// Sequence | OBJECT, NUMBER | STRING -> DATUM
	INNER_JOIN          ( 48),// Sequence, Sequence, Function(2) -> Sequence
	OUTER_JOIN          ( 49),// Sequence, Sequence, Function(2) -> Sequence
	EQ_JOIN             ( 50),// Sequence, !STRING, Sequence, {index:!STRING} -> Sequence
	ZIP                 ( 72),// Sequence -> Sequence
	INSERT_AT           ( 82),// ARRAY, NUMBER, DATUM -> ARRAY
	DELETE_AT           ( 83),// ARRAY, NUMBER -> ARRAY | ARRAY, NUMBER, NUMBER -> ARRAY
	CHANGE_AT           ( 84),// ARRAY, NUMBER, DATUM -> ARRAY
	SPLICE_AT           ( 85),// ARRAY, NUMBER, ARRAY -> ARRAY
	COERCE_TO           ( 51),// Top, STRING -> Top
	TYPEOF              ( 52),// Top -> STRING
	UPDATE              ( 53),// StreamSelection, Function(1), {non_atomic:BOOL, durability:STRING, return_changes:BOOL} -> OBJECT | SingleSelection, Function(1), {non_atomic:BOOL, durability:STRING, return_changes:BOOL} -> OBJECT | StreamSelection, OBJECT,      {non_atomic:BOOL, durability:STRING, return_changes:BOOL} -> OBJECT | SingleSelection, OBJECT,      {non_atomic:BOOL, durability:STRING, return_changes:BOOL} -> OBJECT
	DELETE              ( 54),// StreamSelection, {durability:STRING, return_changes:BOOL} -> OBJECT | SingleSelection -> OBJECT
	REPLACE             ( 55),// StreamSelection, Function(1), {non_atomic:BOOL, durability:STRING, return_changes:BOOL} -> OBJECT | SingleSelection, Function(1), {non_atomic:BOOL, durability:STRING, return_changes:BOOL} -> OBJECT
	INSERT              ( 56),// Table, OBJECT, {conflict:STRING, durability:STRING, return_changes:BOOL} -> OBJECT | Table, Sequence, {conflict:STRING, durability:STRING, return_changes:BOOL} -> OBJECT
	DB_CREATE           ( 57),// STRING -> OBJECT
	DB_DROP             ( 58),// STRING -> OBJECT
	DB_LIST             ( 59),// -> ARRAY
	TABLE_CREATE        ( 60),// Database, STRING, {datacenter:STRING, primary_key:STRING, durability:STRING} -> OBJECT
	TABLE_DROP          ( 61),// Database, STRING -> OBJECT
	TABLE_LIST          ( 62),// Database -> ARRAY
	SYNC                (138),// Table -> OBJECT
	INDEX_CREATE        ( 75),// Table, STRING, Function(1), {multi:BOOL} -> OBJECT
	INDEX_DROP          ( 76),// Table, STRING -> OBJECT
	INDEX_LIST          ( 77),// Table -> ARRAY
	INDEX_STATUS        (139),// Table, STRING... -> ARRAY
	INDEX_WAIT          (140),// Table, STRING... -> ARRAY
	INDEX_RENAME        (156),// Table, STRING, STRING, {overwrite:BOOL} -> OBJECT
	FUNCALL             ( 64),// Function(*), DATUM... -> DATUM
	BRANCH              ( 65),// BOOL, Top, Top -> Top
	ANY                 ( 66),// BOOL... -> BOOL
	ALL                 ( 67),// BOOL... -> BOOL
	FOREACH             ( 68),// Sequence, Function(1) -> OBJECT
	FUNC                ( 69),// ARRAY, Top -> ARRAY -> Top
	ASC                 ( 73),// !STRING -> Ordering
	DESC                ( 74),// !STRING -> Ordering
	INFO                ( 79),// Top -> OBJECT
	MATCH               ( 97),// STRING, STRING -> DATUM
	UPCASE              (141),// STRING -> STRING
	DOWNCASE            (142),// STRING -> STRING
	SAMPLE              ( 81),// Sequence, NUMBER -> Sequence
	DEFAULT             ( 92),// Top, Top -> Top
	JSON                ( 98),// STRING -> DATUM
	ISO8601             ( 99),// STRING -> PSEUDOTYPE(TIME)
	TO_ISO8601          (100),// PSEUDOTYPE(TIME) -> STRING
	EPOCH_TIME          (101),// NUMBER -> PSEUDOTYPE(TIME)
	TO_EPOCH_TIME       (102),// PSEUDOTYPE(TIME) -> NUMBER
	NOW                 (103),// -> PSEUDOTYPE(TIME)
	IN_TIMEZONE         (104),// PSEUDOTYPE(TIME), STRING -> PSEUDOTYPE(TIME)
	DURING              (105),// PSEUDOTYPE(TIME), PSEUDOTYPE(TIME), PSEUDOTYPE(TIME) -> BOOL
	DATE                (106),// PSEUDOTYPE(TIME) -> PSEUDOTYPE(TIME)
	TIME_OF_DAY         (126),// PSEUDOTYPE(TIME) -> NUMBER
	TIMEZONE            (127),// PSEUDOTYPE(TIME) -> STRING
	YEAR                (128),// PSEUDOTYPE(TIME) -> NUMBER
	MONTH               (129),// PSEUDOTYPE(TIME) -> NUMBER
	DAY                 (130),// PSEUDOTYPE(TIME) -> NUMBER
	DAY_OF_WEEK         (131),// PSEUDOTYPE(TIME) -> NUMBER
	DAY_OF_YEAR         (132),// PSEUDOTYPE(TIME) -> NUMBER
	HOURS               (133),// PSEUDOTYPE(TIME) -> NUMBER
	MINUTES             (134),// PSEUDOTYPE(TIME) -> NUMBER
	SECONDS             (135),// PSEUDOTYPE(TIME) -> NUMBER
	TIME                (136),// NUMBER, NUMBER, NUMBER -> PSEUDOTYPE(TIME) | NUMBER, NUMBER, NUMBER, STRING -> PSEUDOTYPE(TIME) | NUMBER, NUMBER, NUMBER, NUMBER, NUMBER, NUMBER -> PSEUDOTYPE(TIME) | NUMBER, NUMBER, NUMBER, NUMBER, NUMBER, NUMBER, STRING -> PSEUDOTYPE(TIME)
	MONDAY              (107),// -> 1
	TUESDAY             (108),// -> 2
	WEDNESDAY           (109),// -> 3
	THURSDAY            (110),// -> 4
	FRIDAY              (111),// -> 5
	SATURDAY            (112),// -> 6
	SUNDAY              (113),// -> 7
	JANUARY             (114),// -> 1
	FEBRUARY            (115),// -> 2
	MARCH               (116),// -> 3
	APRIL               (117),// -> 4
	MAY                 (118),// -> 5
	JUNE                (119),// -> 6
	JULY                (120),// -> 7
	AUGUST              (121),// -> 8
	SEPTEMBER           (122),// -> 9
	OCTOBER             (123),// -> 10
	NOVEMBER            (124),// -> 11
	DECEMBER            (125),// -> 12
	LITERAL             (137),// JSON -> Merging
	GROUP               (144),
	SUM                 (145),
	AVG                 (146),
	MIN                 (147),
	MAX                 (148),
	SPLIT               (149),// STRING -> ARRAY | STRING, STRING -> ARRAY | STRING, STRING, NUMBER -> ARRAY | STRING, NULL, NUMBER -> ARRAY
	UNGROUP             (150),// GROUPED_DATA -> ARRAY
	RANDOM              (151),// NUMBER, NUMBER {float:BOOL} -> DATUM
	CHANGES             (152),// TABLE -> STREAM
	ARGS                (154),// ARRAY -> SPECIAL (used to splice arguments)
	BINARY              (155),// STRING -> PSEUDOTYPE(BINARY)
	GEOJSON             (157),// OBJECT -> PSEUDOTYPE(GEOMETRY)
	TO_GEOJSON          (158),// PSEUDOTYPE(GEOMETRY) -> OBJECT
	POINT               (159),// NUMBER, NUMBER -> PSEUDOTYPE(GEOMETRY)
	LINE                (160),// (ARRAY | PSEUDOTYPE(GEOMETRY))... -> PSEUDOTYPE(GEOMETRY)
	POLYGON             (161),// (ARRAY | PSEUDOTYPE(GEOMETRY))... -> PSEUDOTYPE(GEOMETRY)
	DISTANCE            (162),// PSEUDOTYPE(GEOMETRY), PSEUDOTYPE(GEOMETRY) {geo_system:STRING, unit:STRING} -> NUMBER
	INTERSECTS          (163),// PSEUDOTYPE(GEOMETRY), PSEUDOTYPE(GEOMETRY) -> BOOL
	INCLUDES            (164),// PSEUDOTYPE(GEOMETRY), PSEUDOTYPE(GEOMETRY) -> BOOL
	CIRCLE              (165),// PSEUDOTYPE(GEOMETRY), NUMBER {num_vertices:NUMBER, geo_system:STRING, unit:STRING, fill:BOOL} -> PSEUDOTYPE(GEOMETRY)
	GET_INTERSECTING    (166),// TABLE, PSEUDOTYPE(GEOMETRY) {index:!STRING} -> StreamSelection
	FILL                (167),// PSEUDOTYPE(GEOMETRY) -> PSEUDOTYPE(GEOMETRY)
	GET_NEAREST         (168),// TABLE, PSEUDOTYPE(GEOMETRY) {index:!STRING, max_results:NUM, max_dist:NUM, geo_system:STRING, unit:STRING} -> ARRAY
	POLYGON_SUB         (171),// PSEUDOTYPE(GEOMETRY), PSEUDOTYPE(GEOMETRY) -> PSEUDOTYPE(GEOMETRY)

	;
	public int index;
	
	TermType(int index) {
		this.index = index;
	}
}
