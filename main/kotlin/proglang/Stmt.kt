package proglang
const val INNER_INDENT_OFFSET = 4
interface Stmt {
    var next: Stmt?
    val lastInSequence: Stmt
        get() {
            var cur: Stmt? = this
            while (cur?.next != null){
                cur = cur.next
            }
            return cur!!
        }
    fun toString(indent: Int): String
    fun clone(): Stmt

    class Assign(val name: String,
                 val expr: IntExpr,
                 override var next: Stmt? = null) : Stmt {
        override fun toString(indent: Int): String {
            val sb = StringBuilder("${" ".repeat(indent)}$name = $expr\n")
            if(next != null) sb.append(next!!.toString(indent))
            return sb.toString()
        }

        override fun toString(): String {
            return toString(indent = 0)
        }

        override fun clone(): Stmt = Assign(name, expr, next?.clone())
    }

    class If(val condition: BoolExpr, val thenStmt: Stmt,
             val elseStmt: Stmt? = null, override var next: Stmt? = null) : Stmt {
        override fun toString(indent: Int): String {
            val sb = StringBuilder()
            sb.append("${" ".repeat(indent)}if ($condition) {\n")
            sb.append(thenStmt.toString(indent + INNER_INDENT_OFFSET))
            sb.append("${" ".repeat(indent)}}")
            if(elseStmt != null){
                sb.append(" else {\n")
                sb.append(elseStmt.toString(indent + INNER_INDENT_OFFSET))
                sb.append("${" ".repeat(indent)}}\n")
            }else sb.append("\n")
            if(next != null) sb.append(next!!.toString(indent))
            return sb.toString()
        }

        override fun toString(): String {
            return toString(indent = 0)
        }

        override fun clone(): Stmt = If(condition, thenStmt.clone(),
            elseStmt?.clone(), next?.clone())
    }

    class While(val condition: BoolExpr, val body: Stmt?, override var next: Stmt? = null): Stmt{
        override fun toString(indent: Int): String {
            val sb = StringBuilder()
            sb.append("${" ".repeat(indent)}while ($condition) {\n")
            if(body != null)
                sb.append(body.toString(indent = indent + INNER_INDENT_OFFSET))
            sb.append("${" ".repeat(indent)}}\n")
            if(next != null)
                sb.append(next!!.toString(indent = indent))
            return sb.toString()
        }

        override fun toString(): String {
            return toString(indent = 0)
        }

        override fun clone(): Stmt {
            return While(condition, body?.clone(), next?.clone())
        }
    }
}

fun Stmt.step(store: MutableMap<String, Int>): Stmt? = when(this) {

    is Stmt.Assign -> {
        try {
            val rhsEval = expr.eval(store)
            store[name] = rhsEval
        }catch (_: UndefinedBehaviourException){
            throw UndefinedBehaviourException("")
        }
        next
    }
    is Stmt.If -> {
        val evalCondition = condition.eval(store)
        var ret: Stmt? = null
        if(evalCondition) {
            thenStmt.lastInSequence.next = next
            ret = thenStmt
        }else{
            if(elseStmt == null) ret = next
            else {
                elseStmt.lastInSequence.next = next
                ret = elseStmt
            }
        }
        ret
    }
    is Stmt.While -> {
        val evalCondition = condition.eval(store)
        var r:Stmt? = null
        if(evalCondition && body != null) {
            val bodyClone = body.clone()
            bodyClone.lastInSequence.next = this
            r = bodyClone
        }else if(evalCondition){
            r = this
        }else{
            r = next
        }
        r
    }
    else -> null
}