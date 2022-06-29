package compilador;

import exceptions.Error;
import java.util.*;

public class Analizador {
    private static final HashMap<String, Integer> table = Token.populaTerminais();
    private static final HashMap<String, Integer> tableNT = Token.populaNaoTerminais();
    private static HashMap<String, String> tabelaParsing = new HashMap<String, String>();
    private static boolean breackLineComent = false;
    private static Token objToken = new Token(0, "", 1);
    private static Token objTokenNT = new Token(0, "", 1);

    public static Error Analize(Stack<Token> finalStack, List<String> lines) {

        int currentLine = 0;
        Stack<String> linesStack = new Stack<String>();
        Stack<String> auxStack = new Stack<String>();
        Queue<Character> characterQueue = new LinkedList<>();
        Queue<Character> auxCharacterQueue = new LinkedList<>();

        for (String line : lines) {
            line = line.trim();
            linesStack.add(line);
        }

        for (String lineStack : linesStack) {
            String aux = "";
            String aux2 = "";
            String aux3 = "";
            String auxComent = "";
            for (Character c : lineStack.toCharArray()) {
                characterQueue.add(c);
            }
            currentLine++;

            auxCharacterQueue.clear();
            auxCharacterQueue.addAll(characterQueue);
            do{
                //linha vasia
                if(characterQueue.isEmpty()) {
                    break;
                }

                if(breackLineComent) {
                    auxCharacterQueue.clear();
                    auxCharacterQueue.add('(');
                    auxCharacterQueue.add('*');
                    for(Character x : characterQueue) {
                        auxCharacterQueue.add(x);
                    }
                    characterQueue.clear();
                    characterQueue.addAll(auxCharacterQueue);
                    breackLineComent = false;
                }

                if (String.valueOf(characterQueue.peek()).equalsIgnoreCase("(")) {
                    auxCharacterQueue.clear();
                    auxCharacterQueue.addAll(characterQueue);

                    aux += String.valueOf(String.valueOf(auxCharacterQueue.poll()));

                    if (String.valueOf(auxCharacterQueue.peek()).toUpperCase(Locale.ROOT).equals("*")) {
                        aux += characterQueue.poll();
                        characterQueue.poll();

                        while (!aux.contains("*)")) {
                            //quebra de linha
                            if(characterQueue.isEmpty()) {
                                breackLineComent = true;
                                break;
                            }

                            aux += String.valueOf(characterQueue.poll());
                        }

                        if(breackLineComent) {
                            break;
                        }

                        aux += String.valueOf(characterQueue.poll());

                        aux = "";
                    }else {
                        aux = "";
                        if (table.containsKey(characterQueue.peek().toString().toUpperCase())) {
                            aux += String.valueOf(characterQueue.poll()).toUpperCase();
                            finalStack.add(new Token(table.get(aux.toUpperCase()), aux.toUpperCase(), currentLine));
                            aux = "";
                        }
                    }
                    aux = "";
                    auxCharacterQueue.clear();
                    auxCharacterQueue.addAll(characterQueue);
                }else if(characterQueue.peek().toString().toUpperCase(Locale.ROOT).equals("'")) {
                    aux += characterQueue.poll().toString();
                    while(!characterQueue.peek().toString().toUpperCase(Locale.ROOT).equals("'")) {
                        aux += characterQueue.poll().toString();
                    }

                    aux += characterQueue.poll().toString();

                    if (aux.length() <= 255) {
                        finalStack.add(new Token(48, aux, currentLine));
                    }else{
                        return new Error (true, "Literal não suporta mais que 255 caracteres", currentLine);
                    }

                    aux = "";

                }else {
                    if (characterQueue.peek().toString().equals(" ")) {
                        characterQueue.poll();
                        if (!aux.equals(" ") && !aux.equals("")) {

                            try {
                                Double.parseDouble(aux);
                                if (Integer.parseInt(aux) >= -32767 || Integer.parseInt(aux) <= 32767) {
                                    finalStack.add(new Token(26, aux, currentLine));
                                } else {
                                    return new Error(true, "Valor inteiro fora da faixa aceita", currentLine);
                                }
                            } catch (NumberFormatException e) {
                                aux3 = String.valueOf(aux.charAt(0));

                                try {
                                    Double.parseDouble(aux3);
                                    return new Error(true, "Identificador não aceita números no inicio", currentLine);
                                } catch (NumberFormatException ee) {

                                    if (aux.length() <= 30) {
                                        finalStack.add(new Token(25, aux, currentLine));
                                    } else {
                                        return new Error(true, "Identificador não aceita mais de 30 caracteres", currentLine);
                                    }
                                }
                            }
                            aux = "";
                        }
                    } else {
                        if (!characterQueue.isEmpty()) {
                            aux2 = characterQueue.poll().toString();

                            if (table.containsKey(aux2.toString().toUpperCase() + characterQueue.peek())) {
                                if (!aux.equals(" ") && !aux.equals("")) {

                                    try {
                                        Double.parseDouble(aux);
                                        if (Integer.parseInt(aux) >= -32767 && Integer.parseInt(aux) <= 32767) {
                                            finalStack.add(new Token(26, aux, currentLine));
                                        } else {
                                            return new Error(true, "Valor inteiro fora da faixa aceita", currentLine);
                                        }
                                    } catch (NumberFormatException e) {
                                        aux3 = String.valueOf(aux.charAt(0));

                                        try {
                                            Double.parseDouble(aux3);
                                            return new Error(true, "Identificador não pode começar com um número", currentLine);
                                        } catch (NumberFormatException ee) {
                                            if (aux.length() <= 30) {
                                                finalStack.add(new Token(25, aux, currentLine));
                                            } else {
                                                return new Error(true, "Identificador não pode conter mais de 30 caracteres", currentLine);
                                            }
                                        }
                                    }
                                    aux = "";
                                }
                                finalStack.add(new Token(table.get(aux2.toString().toUpperCase() + characterQueue.peek()), aux2.toString().toUpperCase() + characterQueue.peek(), currentLine));
                                characterQueue.poll();
                            } else if (table.containsKey(aux2.toUpperCase())) {

                                if (!aux.equals(" ") && !aux.equals("")) {

                                    try {
                                        Double.parseDouble(aux);
                                        if (Integer.parseInt(aux) >= -32767 && Integer.parseInt(aux) <= 32767) {
                                            finalStack.add(new Token(26, aux, currentLine));
                                        } else {
                                            return new Error(true, "Valor inteiro fora da faixa aceita", currentLine);
                                        }
                                    } catch (NumberFormatException e) {
                                        aux3 = String.valueOf(aux.charAt(0));

                                        try {
                                            Double.parseDouble(aux3);
                                            return new Error(true, "Identificador não pode começar com um número", currentLine);
                                        } catch (NumberFormatException ee) {
                                            if (aux.length() <= 30) {
                                                finalStack.add(new Token(25, aux, currentLine));
                                            } else {
                                                return new Error(true, "Identificador não pode conter mais de 30 caracteres", currentLine);
                                            }
                                        }
                                    }
                                    aux = "";
                                }else if(aux2.equals("-")) {
                                    if(isInteger(characterQueue.peek())) {
                                        while (!String.valueOf(characterQueue.peek()).toUpperCase(Locale.ROOT).equals(";")) {
                                            aux2 += String.valueOf(characterQueue.poll());
                                        }
                                        finalStack.add(new Token(26, aux2, currentLine));
                                        aux2 = "";
                                    }
                                }
                                if(aux2.isEmpty()) {
                                    continue;
                                }
                                finalStack.add(new Token(table.get(aux2.toUpperCase()), aux2.toUpperCase(), currentLine));
                            } else {
                                aux += aux2.toString();

                                if (table.containsKey(aux.toUpperCase())) {

                                    if(String.valueOf(characterQueue.peek()).equalsIgnoreCase(" ")) {
                                        finalStack.add(new Token(table.get(aux.toUpperCase()), aux.toUpperCase(), currentLine));
                                        aux = "";
                                    }

                                } else if (characterQueue.isEmpty()) {
                                    if (!aux.equals(" ") && !aux.equals("")) {

                                        try {
                                            Double.parseDouble(aux);
                                            if (Integer.parseInt(aux) >= -32767 && Integer.parseInt(aux) <= 32767) {
                                                finalStack.add(new Token(26, aux, currentLine));
                                            } else {
                                                return new Error(true, "Valor inteiro fora da faixa aceita", currentLine);
                                            }
                                        } catch (NumberFormatException e) {
                                            aux3 = String.valueOf(aux.charAt(0));

                                            try {
                                                Double.parseDouble(aux3);
                                                return new Error(true, "Identificador não pode começar com um número", currentLine);
                                            } catch (NumberFormatException ee) {
                                                if (aux.length() <= 30) {
                                                    finalStack.add(new Token(25, aux, currentLine));
                                                } else {
                                                    return new Error(true, "Identificador não pode conter mais de 30 caracteres", currentLine);
                                                }
                                            }
                                        }
                                        aux = "";
                                    }
                                }

                            }
                        }
                    }
                }

            } while (!characterQueue.isEmpty());

            characterQueue.clear();
            auxCharacterQueue.clear();
        }

        return new Error(false);
    }

    public static Error analizadorSintaticoError(Stack<Token> finalStack, Stack<Token> nTerminais) {
        Stack<Token> auxP = new Stack<Token>();
        if(finalStack.isEmpty()) {
            return new Error(true, "Insira um codigo", 0);
        }

        Token.implementaTabelaParsing(tabelaParsing);
        objToken = finalStack.peek();
        objTokenNT = nTerminais.peek();
        if(finalStack.peek().getType() == nTerminais.peek().getType()) {
            finalStack.pop();
            nTerminais.pop();
            return new Error(false);
        }

        if(nTerminais.peek().getType() > 51) {
            try {
                if (!tabelaParsing.get("" + nTerminais.peek().getType() + "," + finalStack.peek().getType()).equalsIgnoreCase("null")) {
                    String simbolosDaProducao = tabelaParsing.get("" + nTerminais.peek().getType() + "," + finalStack.peek().getType());
                    nTerminais.pop();

                    String[] aux = simbolosDaProducao.split("&&");

                    for (String nTerminalDaProducao : aux) {
                        int type;
                        if (Token.populaTerminais().get(nTerminalDaProducao) != null) {
                            type = Token.populaTerminais().get(nTerminalDaProducao);
                        } else {
                            type = Token.populaNaoTerminais().get(nTerminalDaProducao);
                        }
                        Token tok = new Token(type, nTerminalDaProducao);
                        auxP.push(tok);
                    }
                    int auxPSize = auxP.size();
                    for (int i = 0; i < auxPSize; i++) {
                        nTerminais.push(auxP.pop());
                    }

                } else if (tabelaParsing.get("" + nTerminais.peek().getType() + "," + finalStack.peek().getType()).equalsIgnoreCase("null")) {
                    nTerminais.pop();
                }
            }catch (Exception e) {
                return new Error(true, objTokenNT.getText(), objToken.getLine());
            }
        }

        return new Error(false);
    }

    public static Stack<Token> invertePilha(Stack<Token> pilha) {
        Stack<Token> auxFinalStack = new Stack<Token>();
        int x = pilha.toArray().length;
        for(int i = 0;i < x; i++) {
            auxFinalStack.push(pilha.pop());
        }
        pilha.clear();
        pilha.addAll(auxFinalStack);
        auxFinalStack.clear();
        return pilha;
    }

    public static boolean isInteger(Character value) {
        if(value == '1') {
            return true;
        }else if(value == '2') {
            return true;
        }else if(value == '3') {
            return true;
        }else if(value == '4') {
            return true;
        }else if(value == '5') {
            return true;
        }else if(value == '6') {
            return true;
        }else if(value == '7') {
            return true;
        }else if(value == '8') {
            return true;
        }else if(value == '9') {
            return true;
        }else{
            return false;
        }
    }
}