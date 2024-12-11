package vtpaoc.y2018.day7;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Worker {

    private static int nextId = 0;
    private int id;
    private Step task;
    private boolean isFree = true;

    Worker(){
        id = nextId++;
    }

    public void startJob(Step workOn){
        this.task = workOn;
        this.isFree = false;
    }

    public Step processJob(){

        Step workedOn = null;

        if (this.task != null) {
            if(this.task.inQueueFor == this.task.takesTime){
                this.task.queued = false;
                this.task.executed = true;
                // finish the job worker wise:
                workedOn = this.task;
                this.task = null;
                this.isFree = true;
            } else {
                this.task.inQueueFor++;
            }
        }

        return workedOn;
    }

}
