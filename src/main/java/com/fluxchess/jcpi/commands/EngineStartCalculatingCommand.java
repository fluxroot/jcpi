/*
 * Copyright 2007-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxchess.jcpi.commands;

import com.fluxchess.jcpi.models.GenericColor;
import com.fluxchess.jcpi.models.GenericMove;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class EngineStartCalculatingCommand implements IEngineCommand {

  private List<GenericMove> searchMoveList = null;
  private boolean ponder = false;
  private final Map<GenericColor, Long> clock = new EnumMap<GenericColor, Long>(GenericColor.class);
  private final Map<GenericColor, Long> clockIncrement = new EnumMap<GenericColor, Long>(GenericColor.class);
  private Integer movestogo = null;
  private Integer depth = null;
  private Long nodes = null;
  private Integer mate = null;
  private Long movetime = null;
  private boolean infinite = false;

  public void accept(IEngine engine) {
    engine.receive(this);
  }

  public List<GenericMove> getSearchMoveList() {
    return this.searchMoveList;
  }

  public void setSearchMoveList(List<GenericMove> searchMoveList) {
    if (searchMoveList == null) throw new IllegalArgumentException();

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
    if (side == null) throw new IllegalArgumentException();
    if (time == null) throw new IllegalArgumentException();

    this.clock.put(side, time);
  }

  public Long getClockIncrement(GenericColor side) {
    return this.clockIncrement.get(side);
  }

  public void setClockIncrement(GenericColor side, Long time) {
    if (side == null) throw new IllegalArgumentException();
    if (time == null) throw new IllegalArgumentException();

    this.clockIncrement.put(side, time);
  }

  public Integer getMovesToGo() {
    return this.movestogo;
  }

  public void setMovesToGo(Integer movesToGo) {
    if (movesToGo == null) throw new IllegalArgumentException();

    this.movestogo = movesToGo;
  }

  public Integer getDepth() {
    return this.depth;
  }

  public void setDepth(Integer depth) {
    if (depth == null) throw new IllegalArgumentException();

    this.depth = depth;
  }

  public Long getNodes() {
    return this.nodes;
  }

  public void setNodes(Long nodes) {
    if (nodes == null) throw new IllegalArgumentException();

    this.nodes = nodes;
  }

  public Integer getMate() {
    return this.mate;
  }

  public void setMate(Integer mate) {
    if (mate == null) throw new IllegalArgumentException();

    this.mate = mate;
  }

  public Long getMoveTime() {
    return this.movetime;
  }

  public void setMoveTime(Long moveTime) {
    if (moveTime == null) throw new IllegalArgumentException();

    this.movetime = moveTime;
  }

  public boolean getInfinite() {
    return this.infinite;
  }

  public void setInfinite() {
    this.infinite = true;
  }

}
