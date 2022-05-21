include includes/macros2.asm
include includes/number.asm
.MODEL
LARGE
.STACK 200h
.386
.DATA
MAXSTRSIZE equ 33

	_var_1	dd	?
	_var_2	dd	?
	_pi	dd	?
	_buffer	db	MAXTEXTSIZE dup (?),'$'
	_3.14159	dd	3.14159
	_4	dd	4
	_6	dd	6
	_2	dd	2
	_3	dd	3
	_Iniciando_Programa	db	"Iniciando Programa",'$', 20 dup (?)
	_1	dd	1
	_Las_variables_coincidieron	db	"Las variables coincidieron",'$', 28 dup (?)
	_var_2_es_mayor_que_pi	db	"var_2 es mayor que pi",'$', 23 dup (?)
	_No_se_cruzaron	db	"No se cruzaron",'$', 16 dup (?)
	_Ingrese_un_valor	db	"Ingrese un valor",'$', 18 dup (?)
	_Usted_ingreso:	db	"Usted ingreso:",'$', 16 dup (?)
	_7	dd	7
	_254	dd	254
	_La_variable_esta_en_la_lista	db	"La variable esta en la lista",'$', 30 dup (?)
	@var_0	dd	?
	@var_2	dd	?
	@var_3	dd	?
	@var_4	dd	?
	@var_5	dd	?
	@var_6	dd	?
	@var_8	dd	?
	@var_9	dd	?
	@var_10	dd	?
	@var_11	dd	?
	@var_12	dd	?
	@var_15	dd	?
	@var_16	dd	?
	@var_17	dd	?
	@var_19	dd	?
	@var_20	dd	?
	@var_21	dd	?
	@var_23	dd	?
	@var_24	dd	?
	@var_27	dd	?
	@var_28	dd	?
	@var_31	dd	?
	@var_32	dd	?
	@var_40	dd	?
	@var_41	dd	?
	@var_44	dd	?
	@var_45	dd	?
	@var_52	dd	?
	@var_55	dd	?
	@var_56	dd	?
	@var_57	dd	?
	@var_60	dd	?
	@var_61	dd	?
	@var_62	dd	?
	@var_63	dd	?
	@var_64	dd	?
	@var_67	dd	?
	@var_68	dd	?
	@var_69	dd	?
	@var_70	dd	?
	@var_71	dd	?

.CODE
.startup

ETQ_0:
	fld _3.14159
	fstp @var_0

ETQ_1:
	fld @var_0
	fstp _pi

ETQ_2:
	fld _4
	fstp @var_2

ETQ_3:
	fld _6
	fstp @var_3

ETQ_4:
	fld _2
	fstp @var_4

ETQ_5:
	fld @var_3
	fld @var_4
	fdiv
	fstp @var_5

ETQ_6:
	fld @var_2
	fld @var_5
	fsub
	fstp @var_6

ETQ_7:
	fld @var_6
	fstp _var_1

ETQ_8:
	fld _2
	fstp @var_8

ETQ_9:
	fld _3
	fstp @var_9

ETQ_10:
	fld @var_8
	fld @var_9
	fadd
	fstp @var_10

ETQ_11:
	fld _2
	fstp @var_11

ETQ_12:
	fld @var_10
	fld @var_11
	fmul
	fstp @var_12

ETQ_13:
	fld @var_12
	fstp _var_2

ETQ_14:
	displayString _Iniciando_Programa
	newLine

ETQ_15:
	fld _var_1
	fstp @var_15

ETQ_16:
	fld _1
	fstp @var_16

ETQ_17:
	fld @var_15
	fld @var_16
	fadd
	fstp @var_17

ETQ_18:
	fld @var_17
	fstp _var_1

ETQ_19:
	fld _var_2
	fstp @var_19

ETQ_20:
	fld _2
	fstp @var_20

ETQ_21:
	fld @var_19
	fld @var_20
	fsub
	fstp @var_21

ETQ_22:
	fld @var_21
	fstp _var_2

ETQ_23:
	fld _var_1
	fstp @var_23

ETQ_24:
	fld _var_2
	fstp @var_24

ETQ_25:
	fld @var_24
	fld @var_23
	fcom
	fstsw ax
	sahf

ETQ_26:
	jz ETQ_31

ETQ_27:
	fld _var_2
	fstp @var_27

ETQ_28:
	fld _pi
	fstp @var_28

ETQ_29:
	fld @var_28
	fld @var_27
	fcom
	fstsw ax
	sahf

ETQ_30:
	jbe ETQ_39

ETQ_31:
	fld _var_1
	fstp @var_31

ETQ_32:
	fld _var_2
	fstp @var_32

ETQ_33:
	fld @var_32
	fld @var_31
	fcom
	fstsw ax
	sahf

ETQ_34:
	jnz ETQ_37

ETQ_35:
	displayString _Las_variables_coincidieron
	newLine

ETQ_36:
	jmp ETQ_38

ETQ_37:
	displayString _var_2_es_mayor_que_pi
	newLine

ETQ_38:
	jmp ETQ_40

ETQ_39:
	displayString _No_se_cruzaron
	newLine

ETQ_40:
	fld _var_1
	fstp @var_40

ETQ_41:
	fld _var_2
	fstp @var_41

ETQ_42:
	fld @var_41
	fld @var_40
	fcom
	fstsw ax
	sahf

ETQ_43:
	ja ETQ_48

ETQ_44:
	fld _var_1
	fstp @var_44

ETQ_45:
	fld _var_2
	fstp @var_45

ETQ_46:
	fld @var_45
	fld @var_44
	fcom
	fstsw ax
	sahf

ETQ_47:
	jnz ETQ_15

ETQ_48:
	displayString _Ingrese_un_valor
	newLine

ETQ_49:
	getString _buffer

ETQ_50:
	displayString _Usted_ingreso:
	newLine

ETQ_51:
	displayString _buffer
	newLine

ETQ_52:
	fld _3.14159
	fstp @var_52

ETQ_53:
	fld @var_52
	fld _pi
	fcom
	fstsw ax
	sahf

ETQ_54:
	jz ETQ_75

ETQ_55:
	fld _7
	fstp @var_55

ETQ_56:
	fld _2
	fstp @var_56

ETQ_57:
	fld @var_55
	fld @var_56
	fmul
	fstp @var_57

ETQ_58:
	fld @var_57
	fld _pi
	fcom
	fstsw ax
	sahf

ETQ_59:
	jz ETQ_75

ETQ_60:
	fld _254
	fstp @var_60

ETQ_61:
	fld _2
	fstp @var_61

ETQ_62:
	fld _1
	fstp @var_62

ETQ_63:
	fld @var_61
	fld @var_62
	fmul
	fstp @var_63

ETQ_64:
	fld @var_60
	fld @var_63
	fadd
	fstp @var_64

ETQ_65:
	fld @var_64
	fld _pi
	fcom
	fstsw ax
	sahf

ETQ_66:
	jz ETQ_75

ETQ_67:
	fld _2
	fstp @var_67

ETQ_68:
	fld _2
	fstp @var_68

ETQ_69:
	fld @var_67
	fld @var_68
	fadd
	fstp @var_69

ETQ_70:
	fld _4
	fstp @var_70

ETQ_71:
	fld @var_69
	fld @var_70
	fmul
	fstp @var_71

ETQ_72:
	fld @var_71
	fld _pi
	fcom
	fstsw ax
	sahf

ETQ_73:
	jz ETQ_75

ETQ_74:
	jmp ETQ_76

ETQ_75:
	displayString _La_variable_esta_en_la_lista
	newLine

END
