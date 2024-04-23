package proglang

sealed interface IntExpr {
    class Add(val lhs: IntExpr, val rhs: IntExpr) : IntExpr {
        override fun toString(): String {
            return lhs.toString() + " + " + rhs.toString()
        }
    }
    class Literal(val value: Int) : IntExpr {
        override fun toString(): String {
            return value.toString()
        }
    }
    class Var(val name: String): IntExpr {
        override fun toString(): String {
            return name
        }
    }
    class Mul(val lhs: IntExpr, val rhs: IntExpr) : IntExpr {
        override fun toString(): String {
            return "$lhs * $rhs"
        }
    }
    class Sub(val lhs: IntExpr, val rhs: IntExpr) : IntExpr {
        override fun toString(): String {
            return "$lhs - $rhs"
        }
    }
    class Fact(val expr:IntExpr): IntExpr {
        override fun toString(): String {
            return "$expr!"
        }
    }
    class Paren(val expr:IntExpr): IntExpr {
        override fun toString(): String {
            return "($expr)"
        }
    }
    class Div(val lhs: IntExpr, val rhs:IntExpr): IntExpr {
        override fun toString(): String {
            return "$lhs / $rhs"
        }
    }
}

fun IntExpr.eval(store: Map<String, Int>): Int = when (this) {
    is IntExpr.Add -> lhs.eval(store) + rhs.eval(store)
    is IntExpr.Literal -> value
    is IntExpr.Var -> store[name] ?: throw UndefinedBehaviourException("")
    is IntExpr.Mul -> lhs.eval(store) * rhs.eval(store)
    is IntExpr.Sub -> lhs.eval(store) - rhs.eval(store)
    is IntExpr.Fact ->{
        val evalExpr = expr.eval(store)
        if(evalExpr < 0)
            throw UndefinedBehaviourException("")
        else if(evalExpr == 0)
            1
        else {
            val n = evalExpr
            val factExpr = IntExpr.Mul(IntExpr.Literal(n), IntExpr.Fact(IntExpr.Literal(n - 1)))
            factExpr.eval(store)
        }
    }

    is IntExpr.Paren -> expr.eval(store)
    is IntExpr.Div ->
        if(rhs.eval(store) == 0)
            throw UndefinedBehaviourException("")
        else
            lhs.eval(store) / rhs.eval(store)
}
