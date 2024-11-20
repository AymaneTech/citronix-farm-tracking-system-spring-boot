package com.wora.citronix.harvest.domain.repository;

import com.wora.citronix.farm.domain.vo.FieldId;
import com.wora.citronix.harvest.domain.entity.HarvestDetail;
import com.wora.citronix.harvest.domain.vo.HarvestDetailId;
import com.wora.citronix.harvest.domain.vo.HarvestId;
import com.wora.citronix.harvest.domain.vo.Season;
import com.wora.citronix.tree.domain.TreeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HarvestDetailRepository extends JpaRepository<HarvestDetail, HarvestDetailId> {
    boolean existsByTreeIdAndHarvestIdAndHarvestSeason(TreeId treeId, HarvestId harvestId, Season season);

    @Query("""
            SELECT CASE WHEN COUNT(hd) > 0 THEN true ELSE false END
            FROM HarvestDetail hd
            JOIN hd.tree t
            WHERE t.field.id = :fieldId
            AND hd.harvest.season = :season
            AND hd.harvest.id <> :currentHarvestId
            """)
    boolean existsByFieldIdAndSeasonExcludingHarvest(
            FieldId fieldId,
            Season season,
            HarvestId currentHarvestId
    );
}
