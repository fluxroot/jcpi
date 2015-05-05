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
package com.fluxchess.jcpi.options;

public final class Options {

  public static SpinnerOption newHashOption(int defaultValue, int minValue, int maxValue) {
    return new SpinnerOption("Hash", defaultValue, minValue, maxValue);
  }

  public static TextboxOption newNalimovPathOption(String defaultValue) {
    return new TextboxOption("NalimovPath", defaultValue);
  }

  public static SpinnerOption newNalimovCacheOption(int defaultValue, int minValue, int maxValue) {
    return new SpinnerOption("NalimovCache", defaultValue, minValue, maxValue);
  }

  public static CheckboxOption newPonderOption(boolean defaultValue) {
    return new CheckboxOption("Ponder", defaultValue);
  }

  public static CheckboxOption newOwnBookOption(boolean defaultValue) {
    return new CheckboxOption("OwnBook", defaultValue);
  }

  public static SpinnerOption newMultiPVOption(int defaultValue, int minValue, int maxValue) {
    return new SpinnerOption("MultiPV", defaultValue, minValue, maxValue);
  }

  public static CheckboxOption newUciShowCurrLineOption(boolean defaultValue) {
    return new CheckboxOption("UCI_ShowCurrLine", defaultValue);
  }

  public static CheckboxOption newUciShowRefutationsOption(boolean defaultValue) {
    return new CheckboxOption("UCI_ShowRefutations", defaultValue);
  }

  public static CheckboxOption newUciLimitStrengthOption(boolean defaultValue) {
    return new CheckboxOption("UCI_LimitStrength", defaultValue);
  }

  public static SpinnerOption newUciEloOption(int defaultValue, int minValue, int maxValue) {
    return new SpinnerOption("UCI_Elo", defaultValue, minValue, maxValue);
  }

  public static CheckboxOption newUciAnalyseModeOption(boolean defaultValue) {
    return new CheckboxOption("UCI_AnalyseMode", defaultValue);
  }

  public static TextboxOption newUciOpponentOption(String defaultValue) {
    return new TextboxOption("UCI_Opponent", defaultValue);
  }

  public static TextboxOption newUciEngineAboutOption(String defaultValue) {
    return new TextboxOption("UCI_EngineAbout", defaultValue);
  }

  public static TextboxOption newUciShredderbasesPathOption(String defaultValue) {
    return new TextboxOption("UCI_ShredderbasesPath", defaultValue);
  }

  public static TextboxOption newUciSetPositionValueOption(String defaultValue) {
    return new TextboxOption("UCI_SetPositionValue", defaultValue);
  }

}
