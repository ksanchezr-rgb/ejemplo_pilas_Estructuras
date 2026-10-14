import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Programa {

    static BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
    static HashMap<String, Double> variables = new HashMap<String, Double>();
    static String expresion = "";
    static String explicacion = "";

    public static void main(String[] args) throws IOException {
        String opcion = "";
        while (!opcion.equals("5")) {
            System.out.println("===== CALCULADORA CON PILAS =====");
            System.out.println("Expresión: " + expresion);
            System.out.println("1. Ingresar la expresión");
            System.out.println("2. Definir una variable");
            System.out.println("3. Ver el resultado");
            System.out.println("4. Ver la explicación");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");
            opcion = entrada.readLine();
            System.out.println();

            if (opcion.equals("1")) {
                ingresarExpresion();
            } else if (opcion.equals("2")) {
                definirVariable();
            } else if (opcion.equals("3")) {
                verResultado();
            } else if (opcion.equals("4")) {
                verExplicacion();
            } else if (!opcion.equals("5")) {
                System.out.println("Opción no válida.");
            }
            System.out.println();
        }
    }

    static void ingresarExpresion() throws IOException {
        System.out.print("Escriba la expresión (ejemplo: (3 + x) * 2): ");
        expresion = entrada.readLine();
        explicacion = "";
    }

    static void definirVariable() throws IOException {
        System.out.print("Nombre de la variable: ");
        String nombre = entrada.readLine();
        System.out.print("Valor de " + nombre + ": ");
        try {
            double valor = Double.parseDouble(entrada.readLine());
            variables.put(nombre, valor);
            System.out.println(nombre + " = " + mostrarNumero(valor));
        } catch (NumberFormatException e) {
            System.out.println("Eso no es un número.");
        }
    }

    static void verResultado() {
        if (expresion.equals("")) {
            System.out.println("Primero ingrese una expresión.");
            return;
        }
        explicacion = "";
        String resultado = calcular();
        if (resultado == null) {
            explicacion = "";
        } else {
            System.out.println("Resultado: " + resultado);
        }
    }

    static void verExplicacion() {
        if (explicacion.equals("")) {
            System.out.println("Primero calcule el resultado (opción 3).");
        } else {
            System.out.println(explicacion);
        }
    }

    // Recorre la expresión usando dos pilas: una de números y otra de operadores
    static String calcular() {
        Pila numeros = new Pila();
        Pila operadores = new Pila();
        int i = 0;

        while (i < expresion.length()) {
            char letra = expresion.charAt(i);

            if (letra == ' ') {
                i++;
            } else if (Character.isDigit(letra)) {
                String numero = "";
                while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero = numero + expresion.charAt(i);
                    i++;
                }
                numeros.push(numero);
                explicacion += "Leo el número " + numero + " y lo guardo en la pila de números.\n";
            } else if (Character.isLetter(letra)) {
                String nombre = "";
                while (i < expresion.length() && Character.isLetter(expresion.charAt(i))) {
                    nombre = nombre + expresion.charAt(i);
                    i++;
                }
                if (!variables.containsKey(nombre)) {
                    System.out.println("La variable " + nombre + " no tiene valor.");
                    return null;
                }
                numeros.push(String.valueOf(variables.get(nombre)));
                explicacion += "Leo la variable " + nombre + " y guardo su valor en la pila de números.\n";
            } else if (letra == '(') {
                operadores.push("(");
                explicacion += "Leo un paréntesis que abre y lo guardo en la pila de operadores.\n";
                i++;
            } else if (letra == ')') {
                explicacion += "Leo un paréntesis que cierra: resuelvo lo que hay hasta el paréntesis que abre.\n";
                while (!operadores.estaVacia() && !operadores.peek().equals("(")) {
                    if (!resolver(numeros, operadores)) {
                        return null;
                    }
                }
                if (operadores.estaVacia()) {
                    System.out.println("Sobra un paréntesis que cierra.");
                    return null;
                }
                operadores.pop();
                i++;
            } else if (letra == '+' || letra == '-' || letra == '*' || letra == '/') {
                String operador = String.valueOf(letra);
                while (!operadores.estaVacia() && prioridad(operadores.peek()) >= prioridad(operador)) {
                    if (!resolver(numeros, operadores)) {
                        return null;
                    }
                }
                operadores.push(operador);
                explicacion += "Leo el operador " + operador + " y lo guardo en la pila de operadores.\n";
                i++;
            } else {
                System.out.println("Símbolo no válido: " + letra);
                return null;
            }
        }

        while (!operadores.estaVacia()) {
            if (operadores.peek().equals("(")) {
                System.out.println("Falta cerrar un paréntesis.");
                return null;
            }
            if (!resolver(numeros, operadores)) {
                return null;
            }
        }

        String resultado = numeros.pop();
        if (resultado == null || !numeros.estaVacia()) {
            System.out.println("La expresión está mal escrita.");
            return null;
        }
        explicacion += "Ya no queda nada por leer. El número que queda en la pila es el resultado.\n";
        return mostrarNumero(Double.parseDouble(resultado));
    }

    static boolean resolver(Pila numeros, Pila operadores) {
        String operador = operadores.pop();
        String segundo = numeros.pop();
        String primero = numeros.pop();

        if (primero == null || segundo == null) {
            System.out.println("La expresión está mal escrita.");
            return false;
        }

        double a = Double.parseDouble(primero);
        double b = Double.parseDouble(segundo);
        double resultado = 0;

        if (operador.equals("+")) {
            resultado = a + b;
        } else if (operador.equals("-")) {
            resultado = a - b;
        } else if (operador.equals("*")) {
            resultado = a * b;
        } else if (operador.equals("/")) {
            if (b == 0) {
                System.out.println("No se puede dividir entre cero.");
                return false;
            }
            resultado = a / b;
        }

        numeros.push(String.valueOf(resultado));
        explicacion += "   Saco " + mostrarNumero(a) + " " + operador + " " + mostrarNumero(b)
                + " = " + mostrarNumero(resultado) + " y guardo el resultado en la pila de números.\n";
        return true;
    }

    static int prioridad(String operador) {
        if (operador.equals("*") || operador.equals("/")) {
            return 2;
        }
        if (operador.equals("+") || operador.equals("-")) {
            return 1;
        }
        return 0;
    }

    static String mostrarNumero(double numero) {
        if (numero == (int) numero) {
            return String.valueOf((int) numero);
        }
        return String.valueOf(numero);
    }
}
