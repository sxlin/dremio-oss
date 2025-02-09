/*
 * Copyright (C) 2017-2019 Dremio Corporation
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
package com.dremio.service.autocomplete.statements.grammar;

import static com.dremio.exec.planner.sql.parser.impl.ParserImplConstants.EXCEPT;
import static com.dremio.exec.planner.sql.parser.impl.ParserImplConstants.INTERSECT;
import static com.dremio.exec.planner.sql.parser.impl.ParserImplConstants.SET_MINUS;
import static com.dremio.exec.planner.sql.parser.impl.ParserImplConstants.UNION;

import org.apache.arrow.util.Preconditions;

import com.dremio.service.autocomplete.statements.visitors.StatementInputOutputVisitor;
import com.dremio.service.autocomplete.statements.visitors.StatementVisitor;
import com.dremio.service.autocomplete.tokens.DremioToken;
import com.dremio.service.autocomplete.tokens.TokenBuffer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * A set of query statements:
 *
 * |   query UNION [ ALL | DISTINCT ] query
 * |   query EXCEPT [ ALL | DISTINCT ] query
 * |   query MINUS [ ALL | DISTINCT ] query
 * |   query INTERSECT [ ALL | DISTINCT ] query
 *
 */
public final class SetQueryStatement extends QueryStatement {
  private static final ImmutableSet<Integer> QUERY_SEPARATORS = new ImmutableSet.Builder<Integer>()
    .add(UNION)
    .add(EXCEPT)
    .add(INTERSECT)
    .add(SET_MINUS)
    .build();

  private SetQueryStatement(
    ImmutableList<DremioToken> tokens,
    ImmutableList<QueryStatement> children) {
    super(tokens, children);
  }

  @Override
  public void accept(StatementVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public <I, O> O accept(StatementInputOutputVisitor<I, O> visitor, I input) {
    return visitor.visit(this, input);
  }

  public static SetQueryStatement parse(TokenBuffer tokenBuffer) {
    Preconditions.checkNotNull(tokenBuffer);

    ImmutableList<DremioToken> tokens = tokenBuffer.toList();
    ImmutableList.Builder<QueryStatement> queryStatementListBuilder = new ImmutableList.Builder<>();
    while (!tokenBuffer.isEmpty()) {
      QueryStatement parsedQueryStatement = parseNext(tokenBuffer);
      queryStatementListBuilder.add(parsedQueryStatement);
      tokenBuffer.read();
    }

    return new SetQueryStatement(tokens, queryStatementListBuilder.build());
  }

  private static QueryStatement parseNext(TokenBuffer tokenBuffer) {
    ImmutableList<DremioToken> tokens = tokenBuffer.readUntilMatchKindsAtSameLevel(QUERY_SEPARATORS);
    TokenBuffer selectQueryTokenBuffer = new TokenBuffer(tokens);
    return SelectQueryStatement.parse(selectQueryTokenBuffer);
  }
}
