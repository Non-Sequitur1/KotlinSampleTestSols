package proglang

sealed interface IntExpr {
    data class Add(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = "$lhs + $rhs"
    }

    data class Mul(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = "$lhs * $rhs"
    }

    data class Sub(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = "$lhs - $rhs"
    }

    data class Div(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = "$lhs / $rhs"
    }

    data class Literal(
        val value: Int,
    ) : IntExpr {
        override fun toString(): String = "$value"
    }

    data class Var(
        val name: String,
    ) : IntExpr {
        override fun toString(): String = "$name"
    }

    data class Paren(
        val expr: IntExpr,
    ) : IntExpr {
        override fun toString(): String = "($expr)"
    }

    data class Fact(
        val expr: IntExpr,
    ) : IntExpr {
        override fun toString(): String = "$expr!"
    }
}

fun IntExpr.eval(store: Map<String, Int>): Int =
    when (this) {
        is IntExpr.Add -> lhs.eval(store) + rhs.eval(store)
        is IntExpr.Sub -> lhs.eval(store) - rhs.eval(store)
        is IntExpr.Mul -> lhs.eval(store) * rhs.eval(store)
        is IntExpr.Div ->
            run {
                if (rhs.eval(store) != 0) {
                    lhs.eval(store) / rhs.eval(store)
                } else {
                    throw UndefinedBehaviourException("Division by zero error")
                }
            }
        is IntExpr.Var -> store.get(name) ?: throw UndefinedBehaviourException("Variable \"${name}\" was not defined")
        is IntExpr.Literal -> value
        is IntExpr.Paren -> expr.eval(store)
        is IntExpr.Fact -> factorial(expr.eval(store))
    }

private fun factorial(factNum: Int): Int =
    when {
        factNum > 1 -> factNum * factorial(factNum - 1)
        (factNum == 0 || factNum == 1) -> 1
        else -> throw UndefinedBehaviourException("Number $factNum is negative and thus has undefined factorial")
    }
