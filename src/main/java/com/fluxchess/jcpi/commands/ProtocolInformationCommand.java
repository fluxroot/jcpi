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

import com.fluxchess.jcpi.models.GenericMove;
import com.fluxchess.jcpi.models.GenericScore;

import java.util.List;

public class ProtocolInformationCommand implements IProtocolCommand {

  private Integer depth = null;
  private Integer maxDepth = null;
  private Long time = null;
  private Long nodes = null;
  private List<GenericMove> moveList = null;
  private Integer pvNumber = null;
  private Integer centipawns = null;
  private Integer mate = null;
  private GenericScore value = null;
  private GenericMove currentMove = null;
  private Integer currentMoveNumber = null;
  private Integer hash = null;
  private Long nps = null;
  private String string = null;
  private List<GenericMove> refutationList = null;

  public void accept(IProtocol protocol) {
    protocol.send(this);
  }

  public Integer getDepth() {
    return this.depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public Integer getMaxDepth() {
    return this.maxDepth;
  }

  public void setMaxDepth(int maxDepth) {
    this.maxDepth = maxDepth;
  }

  public Long getTime() {
    return this.time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public Long getNodes() {
    return this.nodes;
  }

  public void setNodes(long nodes) {
    this.nodes = nodes;
  }

  public List<GenericMove> getMoveList() {
    return this.moveList;
  }

  public void setMoveList(List<GenericMove> moveList) {
    if (moveList == null) throw new IllegalArgumentException();

    this.moveList = moveList;
  }

  public Integer getPvNumber() {
    return this.pvNumber;
  }

  public void setPvNumber(int pvNumber) {
    this.pvNumber = pvNumber;
  }

  public Integer getCentipawns() {
    return this.centipawns;
  }

  public void setCentipawns(int centipawns) {
    this.centipawns = centipawns;
  }

  public Integer getMate() {
    return this.mate;
  }

  public void setMate(int mate) {
    this.mate = mate;
  }

  public GenericScore getValue() {
    return this.value;
  }

  public void setValue(GenericScore value) {
    if (value == null) throw new IllegalArgumentException();

    this.value = value;
  }

  public GenericMove getCurrentMove() {
    return this.currentMove;
  }

  public void setCurrentMove(GenericMove currentMove) {
    if (currentMove == null) throw new IllegalArgumentException();

    this.currentMove = currentMove;
  }

  public Integer getCurrentMoveNumber() {
    return this.currentMoveNumber;
  }

  public void setCurrentMoveNumber(int currentMoveNumber) {
    this.currentMoveNumber = currentMoveNumber;
  }

  public Integer getHash() {
    return this.hash;
  }

  public void setHash(int hash) {
    this.hash = hash;
  }

  public Long getNps() {
    return this.nps;
  }

  public void setNps(long nps) {
    this.nps = nps;
  }

  public String getString() {
    return this.string;
  }

  public void setString(String string) {
    if (string == null) throw new IllegalArgumentException();

    this.string = string;
  }

  public List<GenericMove> getRefutationList() {
    return this.refutationList;
  }

  public void setRefutationList(List<GenericMove> refutationList) {
    if (refutationList == null) throw new IllegalArgumentException();

    this.refutationList = refutationList;
  }

}
