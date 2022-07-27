package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag",
        primaryKeys = {"sample_id", "label"},
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class TagRecord {

    @ColumnInfo(name = "sample_id")
    @NonNull
    private Long sampleId;

    @NonNull
    private Integer label;

    public TagRecord(Long sampleId, Integer label) {
        this.sampleId = sampleId;
        this.label = label;
    }

    public Integer getLabel() {
        return label;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }
}
