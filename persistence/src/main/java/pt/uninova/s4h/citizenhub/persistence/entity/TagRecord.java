package pt.uninova.s4h.citizenhub.persistence.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag",
        foreignKeys = {
                @ForeignKey(
                        entity = SampleRecord.class,
                        parentColumns = "id",
                        childColumns = "sample_id")
        },
        indices = @Index("sample_id")
)
public class TagRecord {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "sample_id")
    private Long sampleId;

    private Integer label;

    public TagRecord(Long id, Long sampleId, Integer label) {
        this.id = id;
        this.sampleId = sampleId;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public Integer getLabel() {
        return label;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }
}
