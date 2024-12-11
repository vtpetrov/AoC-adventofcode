package vtpaoc.y2018.day7;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
class Step implements Comparable{

    String name;
//    Set<Step> dependents;
    Set<Step> prereqs = new TreeSet<>();
    int takesTime;
    boolean queued = false;
    boolean executed = false;
    int inQueueFor;

    Step(String name) {
        this.name = name;
        this.takesTime = name.getBytes()[0] - 64 + TheSumOfItsParts.PENALTY_TIME;
    }

    public void queue(){
        this.queued = true;
        this.inQueueFor = 1;
    }

//
//    public void addDependent(Step dependent) {
//        if(this.dependents == null) this.dependents = new TreeSet<>();
//
//        this.dependents.add(dependent);
//    }

    public void addPrerequisite(Step prereq) {
        this.prereqs.add(prereq);
    }

    /**
     * - remove as prerequisite for others,
     *
     * @param source
     */
    public void completeThisJob(Map<String, Step> source){

        TheSumOfItsParts.orderOfStepsP2.append(this.name);
        // update prereqs - remove executed step from prereqs of other steps
        for (Step s : source.values()) {
            s.getPrereqs().remove(this);
        }

    }

    @Override
    public int compareTo(Object o) {

        return this.name.compareTo(((Step)o).name);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (prereqs != null) {
            for (Step prereq : prereqs) {
                sb.append(" ").append(prereq.name);
            }
        }

        sb.append(" <- ").append(name);
//        .append(" -> ");

//        if (dependents != null) {
//            for (Step child : dependents) {
//                sb.append(child.name).append(" ");
//            }
//        }

        sb.append("; ").append("takesTime= ").append(this.takesTime).append(", Queued= ").append(queued)
                .append(", inQueueFor= ").append(inQueueFor).append(", executed= ").append(executed).append(";;\n");

        return sb.toString();

    }

    @Override
    public boolean equals(Object obj) {
        return name.equals(((Step)obj).name);
    }
}
