/*
** Copyright 2007-2012 Phokham Nonava
**
** Licensed under the Apache License, Version 2.0 (the "License");
** you may not use this file except in compliance with the License.
** You may obtain a copy of the License at
**
**     http://www.apache.org/licenses/LICENSE-2.0
**
** Unless required by applicable law or agreed to in writing, software
** distributed under the License is distributed on an "AS IS" BASIS,
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
** See the License for the specific language governing permissions and
** limitations under the License.
*/
package jcpi.commands;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jcpi.IEngine;
import jcpi.data.GenericColor;
import jcpi.data.GenericMove;

public class EngineStartCalculatingCommand implements IEngineCommand {

    private List<GenericMove> searchMoveList = null;
    private boolean ponder = false;
    private Map<GenericColor, Long> clock = new EnumMap<GenericColor, Long>(GenericColor.class);
    private Map<GenericColor, Long> clockIncrement = new EnumMap<GenericColor, Long>(GenericColor.class);
    private Integer movestogo = null;
    private Integer depth = null;
    private Long nodes = null;
    private Integer mate = null;
    private Long movetime = null;
    private boolean infinite = false;

    public EngineStartCalculatingCommand() {
    }

    public void accept(IEngine v) {
        v.visit(this);
    }

    public List<GenericMove> getSearchMoveList() {
        return this.searchMoveList;
    }

    public void setSearchMoveList(List<GenericMove> searchMoveList) {
        Objects.requireNonNull(searchMoveList);

        this.searchMoveList = searchMoveList;
    }

    public boolean getPonder() {
        return this.ponder;
    }

    public void setPonder() {
        this.ponder = true;
    }

    public Long getClock(GenericColor side) {
        return this.clock.get(side);
    }

    public void setClock(GenericColor side, Long time) {
        Objects.requireNonNull(side);
        Objects.requireNonNull(time);

        this.clock.put(side, time);
    }

    public Long getClockIncrement(GenericColor side) {
        return this.clockIncrement.get(side);
    }

    public void setClockIncrement(GenericColor side, Long time) {
        Objects.requireNonNull(side);
        Objects.requireNonNull(time);

        this.clockIncrement.put(side, time);
    }

    public Integer getMovesToGo() {
        return this.movestogo;
    }

    public void setMovesToGo(Integer movesToGo) {
        Objects.requireNonNull(movesToGo);

        this.movestogo = movesToGo;
    }

    public Integer getDepth() {
        return this.depth;
    }

    public void setDepth(Integer depth) {
        Objects.requireNonNull(depth);

        this.depth = depth;
    }

    public Long getNodes() {
        return this.nodes;
    }

    public void setNodes(Long nodes) {
        Objects.requireNonNull(nodes);

        this.nodes = nodes;
    }

    public Integer getMate() {
        return this.mate;
    }

    public void setMate(Integer mate) {
        Objects.requireNonNull(mate);

        this.mate = mate;
    }

    public Long getMoveTime() {
        return this.movetime;
    }

    public void setMoveTime(Long moveTime) {
        Objects.requireNonNull(moveTime);

        this.movetime = moveTime;
    }

    public boolean getInfinite() {
        return this.infinite;
    }

    public void setInfinite() {
        this.infinite = true;
    }

}
