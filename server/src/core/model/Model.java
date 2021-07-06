package core.model;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public abstract class Model<T> extends HashMap<String, String> {
    private final HashMap<String, Object> InputData = new HashMap<>();
    protected HashMap<String, String> validator = new HashMap<>();
    private List<String> timestamp= Arrays.asList("createdAt","updatedAt");
    protected List<String> columns;
    protected List<String> requiredColumns;
    protected List<String> uniqueColumns;
    protected String tableName = getClass().getName().split("\\.")[2];


    public final void addData(String column, Object data) {
        if (columns.contains(column) ||timestamp.contains(column)) {
            if (!validator.containsKey(column) || data.toString().matches(validator.get(column)))
                this.InputData.put(column, data);
            else
                throw new NotValidDataException();
        } else
            throw new NotFindColumnException();
    }

    public final boolean save() {
        File root = new File("src/data");
        if (!root.isDirectory())
            root.mkdirs();

        try (PrintWriter out = new PrintWriter(new BufferedOutputStream(new FileOutputStream("src/data/" + tableName + ".db", true)))) {
            boolean find = true;
            if (requiredColumns != null) {
                for (var c : requiredColumns) {
                    if (!InputData.containsKey(c)) {
                        find = false;
                        break;
                    }
                }
                if (!find)
                    throw new RequirementDataException();

            }
            find = true;
            if (uniqueColumns != null) {
                for (var c : uniqueColumns) {
                    if (this.where(c, InputData.get(c)).size() > 0) {
                        find = false;
                        break;
                    }
                }
                if (!find)
                    throw new UniqueDataException();

            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.now();
            this.addData("createdAt",formatter.format(localDateTime));
            this.addData("updatedAt",formatter.format(localDateTime));
            out.println(InputData.toString().replace("{", "").replace("}", ""));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public final List<T> all() {
        List<T> data = new ArrayList<>();
        try (BufferedReader sc = new BufferedReader(new FileReader("src/data/" + tableName + ".db"))) {
            String line;
            while ((line = sc.readLine()) != null) {
                var splitData = line.split(",");
                for (String d : splitData) {
                    String key = d.split("=")[0].trim();
                    String value = d.split("=")[1].trim();
                    this.put(key, value);
                }
                data.add((T) this.clone());
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data;
    }

    public final List<T> where(String column, Object value) {
        var data = all();
        assert data != null;
        data.removeIf(item -> !((Model) item).get(column).equals(value.toString()));


        return data;
    }

    public final void update(String column, Object value) {
        try {
            BufferedReader sc = new BufferedReader(new FileReader("src/data/" + tableName + ".db"));
            String line, fLine = null;
            StringBuffer inputBuffer = new StringBuffer();
            while ((line = sc.readLine()) != null) {
                boolean find = true;
                inputBuffer.append(line);
                inputBuffer.append('\n');

                var splitData = line.split(",");
                for (String d : splitData) {
                    String k = d.split("=")[0].trim();
                    String v = d.split("=")[1].trim();
                    if (!this.get(k).equals(v))
                        find = false;
                }
                if (find)
                    fLine = line;
            }
            sc.close();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.now();
            this.addData("updatedAt",formatter.format(localDateTime));
            this.put(column, value.toString());

            String updated = this.toString().replace("{", "").replace("}", "");

            String inputStr = inputBuffer.toString();
            inputStr = inputStr.replaceAll(fLine, updated);
            FileOutputStream fileOut = new FileOutputStream("src/data/" + tableName + ".db");
            fileOut.write(inputStr.getBytes());
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected class hasMany<Type> {
        private final Model<Type> relation;
        private final String foreignKey, destinationKey;

        public hasMany(Model<Type> relation, String foreignKey, String destinationKey) {
            this.relation = relation;
            this.foreignKey = foreignKey;
            this.destinationKey = destinationKey;
        }

        public List<Type> get() {

            var foreign = Model.this;
            return relation.where(destinationKey, foreign.get(foreignKey));
        }
    }

    protected class hasOne<Type> {
        private final Model<Type> relation;
        private final String foreignKey, destinationKey;

        public hasOne(Model<Type> relation, String foreignKey, String destinationKey) {
            this.relation = relation;
            this.foreignKey = foreignKey;
            this.destinationKey = destinationKey;
        }

        public Type get() {

            var foreign = Model.this;
            return relation.where(destinationKey, foreign.get(foreignKey)).get(0);
        }
    }

    public static class NotFindColumnException extends RuntimeException {
    }

    public static class NotValidDataException extends RuntimeException {
    }

    public static class RequirementDataException extends RuntimeException {
    }

    public static class UniqueDataException extends RuntimeException {
    }
}
