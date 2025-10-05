package com.parser.declaration.parselets;

import com.parser.Parser;
import com.parser.declaration.Declaration;

public interface DeclarationParselet {
    public Declaration parse(Parser par);

}
