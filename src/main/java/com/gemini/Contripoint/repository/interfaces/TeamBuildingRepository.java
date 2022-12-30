package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.TeamBuilding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TeamBuildingRepository extends JpaRepository<TeamBuilding, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from team_building where id = ?1")
    void deleteTeamBuilding(int teamBuildingId);
}
