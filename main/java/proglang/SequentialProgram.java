package proglang;
import java.util.HashMap;
import java.util.Map;


public final class SequentialProgram {

    private final Stmt topStmt;
    public SequentialProgram(Stmt topStmt) {
        this.topStmt = topStmt;
    }
    public Map<String, Integer> execute(Map<String, Integer> initialStore){
        Map<String, Integer> workingStore = new HashMap<>(initialStore);
        Stmt cur = topStmt;
//        System.out.println(cur);
        while (cur != null){
//            System.out.println(cur);
//            System.out.println("----");
            cur = StmtKt.step(cur, workingStore);
//            System.out.println(cur);
//            System.out.println("----");
        }
        return workingStore;
    }

    @Override
    public String toString() {
        return topStmt.toString();
    }
}
