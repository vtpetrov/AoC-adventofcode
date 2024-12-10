package y2024.day09;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class MyDiskEntity {
    int id;
    int sizeInBlocks;
    int beginIdx;
    int endIdx;
    boolean isFile;

    public MyDiskEntity(int idInp, int sizeInp, int endIdxInp, boolean isFile) {
        this.id = idInp;
        this.sizeInBlocks = sizeInp;
        this.endIdx = endIdxInp;
        this.beginIdx = endIdx - sizeInBlocks + 1;
        this.isFile = isFile;
    }


}
