package compilador;

import exceptions.Error;
import java.util.*;

public class Analizador {
    private static final HashMap<String, Integer> table = Token.populaTerminais();
    private static final HashMap<String, Integer> tableNT = Token.populaNaoTerminais();
    private static HashMap<String, String> tabelaParsing = new HashMap<String, String>();
    private static boolean breackLineComent = false;

    public static Error Analize(Stack<Token> finalStack, List<String> lines) {

        int currentLine = 0;
        Stack<String> linesStack = new Stack<String>();
        Stack<String> auxStack = new Stack<String>();
        Queue<Character> characterQueue = new LinkedList<>();
        Queue<Character> auxCharacterQueue = new LinkedList<>();

        for (String line : lines) {
            linesStack.add(line);
        }

        for (String lineStack : linesStack) {
            String aux = "";
            String aux2 = "";
            String aux3 = "";
            for (Character c : lineStack.toCharArray()) {
                characterQueue.add(c);
            }
            currentLine++;
            auxCharacterQueue.clear();
            auxCharacterQueue.addAll(characterQueue);
            do{
                if(breackLineComent) {
                    characterQueue.add('(');
                    characterQueue.add('*');
                }

                if(characterQueue.peek().toString().toUpperCase(Locale.ROOT).equals("'")) {
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

                }else if (String.valueOf(characterQueue.peek()).toUpperCase(Locale.ROOT).equals("(")) {
                    auxCharacterQueue.clear();
                    auxCharacterQueue.addAll(characterQueue);

                    aux += String.valueOf(auxCharacterQueue.poll());

                    if (String.valueOf(auxCharacterQueue.peek()).toUpperCase(Locale.ROOT).equals("*")) {
                        aux += characterQueue.poll();
                        characterQueue.poll();

                        while (!String.valueOf(characterQueue.peek()).toUpperCase(Locale.ROOT).equals(")")) {
                            aux += String.valueOf(characterQueue.poll());
                            if(characterQueue.isEmpty()) {
                                breackLineComent = true;
                                break;
                            }
                        }
                        if(String.valueOf(characterQueue.peek()).toUpperCase(Locale.ROOT).equals(")")) {
                            breackLineComent = false;
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
                } else {
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
                                    finalStack.add(new Token(table.get(aux.toUpperCase()), aux.toUpperCase(), currentLine));
                                    aux = "";
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
        if(finalStack.isEmpty()) {
            return new Error("Insira um codigo", 0);
        }

        Token.implementaTabelaParsing(tabelaParsing);

        if(finalStack.peek().getType() == nTerminais.peek().getType()) {
            finalStack.pop();
            nTerminais.pop();
            return new Error(false);
        }

        if(nTerminais.peek().getType() > 51) {
            if(tabelaParsing.get("" + nTerminais.peek().getType() + "," + finalStack.peek().getType()) != null) {
                String simbolosDaProducao = tabelaParsing.get("" + nTerminais.peek().getType() + "," + finalStack.peek().getType());
                nTerminais.pop();

                String[] aux = simbolosDaProducao.split("&&");
                aux = invertePilha(aux);

                for (String nTerminalDaProducao : aux) {
                    int type;
                    if(Token.populaTerminais().get(nTerminalDaProducao) != null) {
                        type = Token.populaTerminais().get(nTerminalDaProducao);
                    }else {
                        type = Token.populaNaoTerminais().get(nTerminalDaProducao);
                    }
                    Token tok = new Token(type, nTerminalDaProducao);
                    nTerminais.push(tok);
                }

            }else if(tabelaParsing.get("" + nTerminais.peek().getType() + "," + finalStack.peek().getType()) == null) {
                nTerminais.pop();
            }
        }

        return new Error(false);
    }


    public static String[] invertePilha(String[] pilha) {
        String[] aux = new String[0];
        for(int i = 0; i <= pilha.length; i ++) {
            for(int j = pilha.length; j == 0; j--) {
                aux[j] = pilha[i];
            }
        }

        return aux;
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