package proglang

sealed interface BoolExpr {
    class LessThan(val lhs: IntExpr, val rhs: IntExpr) : BoolExpr {
        override fun toString(): String {
            return lhs.toString() + " < " + rhs.toString()
        }
    }
    class GreaterThan(val lhs: IntExpr, val rhs: IntExpr) : BoolExpr {
        override fun toString(): String {
            return "$lhs > $rhs"
        }
    }
    class Equals(val lhs: IntExpr, val rhs: IntExpr): BoolExpr {
        override fun toString(): String {
            return "$lhs == $rhs"
        }
    }
    class And(val lhs: BoolExpr, val rhs: BoolExpr) : BoolExpr {
        override fun toString(): String {
            return "$lhs && $rhs"
        }
    }
    class Or(val lhs: BoolExpr, val rhs: BoolExpr) : BoolExpr {
        override fun toString(): String {
            return "$lhs || $rhs"
        }
    }
    class Not(val expr:BoolExpr): BoolExpr {
        override fun toString(): String {
            return "!$expr"
        }
    }
    class Paren(val expr:BoolExpr): BoolExpr {
        override fun toString(): String {
            return "("+"$expr"+")"
        }
    }
}

fun BoolExpr.eval(store: Map<String, Int>): Boolean = when (this) {
    is BoolExpr.LessThan -> lhs.eval(store) < rhs.eval(store)
    is BoolExpr.GreaterThan -> lhs.eval(store) > rhs.eval(store)
    is BoolExpr.Equals -> lhs.eval(store) == rhs.eval(store)
    is BoolExpr.And -> if(!lhs.eval(store)) false else rhs.eval(store)
    is BoolExpr.Or -> if(lhs.eval(store)) true else rhs.eval(store)
    is BoolExpr.Not ->{
        val evalExpr = expr.eval(store)
        !evalExpr
    }
    is BoolExpr.Paren -> expr.eval(store)
}
