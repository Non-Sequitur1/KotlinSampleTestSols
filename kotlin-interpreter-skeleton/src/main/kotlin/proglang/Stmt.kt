package proglang

private val DEFAULT_INDENT_SPACING: Int = 4

private fun repeatString(
    s: String,
    t: Int,
) = List<String>(t) { s }.joinToString(separator = "")

sealed interface Stmt : Cloneable {
    // Interface Properties and Methods

    var next: Stmt?
    val lastInSequence: Stmt
        get() {
            var curr: Stmt = this
            while (curr.next != null) curr = curr.next!!
            return curr
        }

    public override fun clone(): Stmt

    fun toString(indent: Int): String

    // Types of Statement

    data class Assign(
        val name: String,
        val expr: IntExpr,
        override var next: Stmt? = null,
    ) : Stmt {
        override fun toString(): String = toString(0)

        override fun toString(indent: Int): String =
            "${repeatString(" ", indent)}$name = ${expr}\n${next?.let { it.toString(indent) } ?: ""}"

        override fun clone(): Assign = Assign(name, expr, next?.clone())
    }

    data class If(
        val cond: BoolExpr,
        val thenStmt: Stmt,
        val elseStmt: Stmt? = null,
        override var next: Stmt? = null,
    ) : Stmt {
        override fun toString(): String = toString(0)

        override fun clone(): Stmt = If(cond, thenStmt.clone(), elseStmt?.clone(), next?.clone())

        override fun toString(indent: Int): String =
            "${repeatString(
                " ",
                indent,
            )}if ($cond) {\n${thenStmt.toString(
                indent + DEFAULT_INDENT_SPACING,
            )}${repeatString(" ",indent)}}${
                elseStmt?.let{ elseStatement ->
                    " else {\n${elseStatement.toString(indent + DEFAULT_INDENT_SPACING)}${repeatString(" ",indent)}}"
                } ?: ""
            }\n${next?.toString(indent) ?: ""}"
    }

    data class While(
        val condition: BoolExpr,
        val body: Stmt?,
        override var next: Stmt? = null,
    ) : Stmt {
        override fun toString(): String = toString(0)

        override fun clone(): While = While(condition, body?.clone(), next?.clone())

        override fun toString(indent: Int): String =
            "${repeatString(
                " ",
                indent,
            )}while ($condition) {\n${body?.toString(
                indent + DEFAULT_INDENT_SPACING,
            ) ?: ""}${repeatString(" ",indent)}}\n${next?.toString(indent) ?: ""}"
    }
}

// Applying step to an assignment statement When applied to an Assign statement,
// the step extension method should:

fun Stmt.step(store: MutableMap<String, Int>): Stmt? =
    when (this) {
        is Stmt.Assign ->
            run {
                store[name] = expr.eval(store)
                next
            }
        is Stmt.If ->
            run {
                val condValue: Boolean = cond.eval(store)
                val condBranch: Stmt? = (
                    if (condValue) {
                        thenStmt
                    } else {
                        elseStmt
                    }
                )
                condBranch?.let {
                    it.lastInSequence.next = next
                    it
                } ?: next
            }
        is Stmt.While ->
            run {
                val condValue: Boolean = condition.eval(store)
                val newStmt: Stmt? =
                    (
                        if (condValue) {
                            body
                        } else {
                            null
                        }
                    )?.clone()
                newStmt?.let {
                    it.lastInSequence.next = this.clone()
                    it
                } ?: next
            }
    }
