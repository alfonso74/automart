USE [Delivery]
GO
/****** Object:  Table [dbo].[AM_TC]    Script Date: 03/09/2015 22:32:56 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AM_TC](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[XFER_NO] [varchar](15) NOT NULL,
	[REF] [varchar](50) NULL,
	[COMMNT_1] [varchar](50) NULL,
	[COMMNT_2] [varchar](50) NULL,
	[COMMNT_3] [varchar](50) NULL,
	[CREATED_DATE] [datetime] NOT NULL,
	[CLOSED_DATE] [datetime] NULL,
	[STATUS] [varchar](1) NOT NULL,
	[USERNAME] [varchar](25) NOT NULL,
	[SYS_LOG] [varchar](255) NULL,
 CONSTRAINT [PK_TC] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[AM_TC_LINES]    Script Date: 03/09/2015 22:32:56 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[AM_TC_LINES](
	[ID] [bigint] IDENTITY(1,1) NOT NULL,
	[XFER_NO] [varchar](15) NOT NULL,
	[XFER_IN_LIN_SEQ_NO] [int] NOT NULL,
	[XFER_LIN_SEQ_NO] [int] NOT NULL,
	[SELECTD] [varchar](1) NOT NULL,
	[COMMNT_1] [varchar](50) NULL,
	[COMMNT_2] [varchar](50) NULL,
	[COMMNT_3] [varchar](50) NULL,
	[PREV_QTY_EXPECTD] [decimal](15, 4) NOT NULL,
	[QTY_RECVD] [decimal](15, 4) NOT NULL,
	[NEW_QTY_EXPECTD] [decimal](15, 4) NOT NULL,
	[EST_UNIT_COST] [decimal](15, 4) NULL,
	[EST_EXT_COST] [decimal](15, 4) NULL,
	[ITEM_NO] [varchar](20) NOT NULL,
	[ITEM_DESCR] [varchar](50) NOT NULL,
	[TC_ID] [bigint] NOT NULL,
 CONSTRAINT [PK_TC_LINES] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  ForeignKey [FK_TC_TC_LINES]    Script Date: 03/09/2015 22:32:56 ******/
ALTER TABLE [dbo].[AM_TC_LINES]  WITH CHECK ADD  CONSTRAINT [FK_TC_TC_LINES] FOREIGN KEY([TC_ID])
REFERENCES [dbo].[AM_TC] ([ID])
GO
ALTER TABLE [dbo].[AM_TC_LINES] CHECK CONSTRAINT [FK_TC_TC_LINES]
GO
