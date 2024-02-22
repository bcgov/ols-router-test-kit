<template>
    <table class="table table-striped table-sm">
        <thead>
        <tr>
            <th v-for="col  in columns" :title="col.description">
                {{ col.title }}
            </th>
        </tr>
        </thead>
        
        <tbody>
        <tr v-for="data in rowdata">
            <td v-for="col in columns">
                {{ data[col.name] }}
            </td>
        </tr>
        </tbody>
    </table>
</template>

<script >

export default {
    props:{
        table:{
            required: true
        }
    },

    computed: {
        columns: function(){
            if (!this.table.data.schema) return [];
            return this.table.data.schema.fields;
        },

        rowdata: function(){
            if (!this.table) return [];
            if (!this.table.data.data) return [];
            return this.table.data.data;
        }
    },

    created() {
        this.table.loadData();
    }
}

</script>