package proglang

sealed interface BoolExpr {
    data class LessThan(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : BoolExpr {
        override fun toString(): String = "$lhs < $rhs"
    }

    data class GreaterThan(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : BoolExpr {
        override fun toString(): String = "$lhs > $rhs"
    }

    data class Equals(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : BoolExpr {
        override fun toString(): String = "$lhs == $rhs"
    }

    data class And(
        val lhs: BoolExpr,
        val rhs: BoolExpr,
    ) : BoolExpr {
        override fun toString(): String = "$lhs && $rhs"
    }

    data class Or(
        val lhs: BoolExpr,
        val rhs: BoolExpr,
    ) : BoolExpr {
        override fun toString(): String = "$lhs || $rhs"
    }

    data class Not(
        val expr: BoolExpr,
    ) : BoolExpr {
        override fun toString(): String = "!$expr"
    }

    data class Paren(
        val expr: BoolExpr,
    ) : BoolExpr {
        override fun toString(): String = "($expr)"
    }
}

fun BoolExpr.eval(store: Map<String, Int>): Boolean =
    when (this) {
        is BoolExpr.LessThan -> lhs.eval(store) < rhs.eval(store)
        is BoolExpr.GreaterThan -> lhs.eval(store) > rhs.eval(store)
        is BoolExpr.Equals -> lhs.eval(store) == rhs.eval(store)
        is BoolExpr.And -> lhs.eval(store) && rhs.eval(store)
        is BoolExpr.Or -> lhs.eval(store) || rhs.eval(store)
        is BoolExpr.Not -> !expr.eval(store)
        is BoolExpr.Paren -> expr.eval(store)
    }
